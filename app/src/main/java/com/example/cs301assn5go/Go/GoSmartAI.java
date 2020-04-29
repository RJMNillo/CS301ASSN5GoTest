package com.example.cs301assn5go.Go;

import android.util.Log;

import com.example.cs301assn5go.R;
import com.example.cs301assn5go.game.GameFramework.GameFramework.GameComputerPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;

/**
 * An AI to be used in Go that makes calculated and smart decisions
 *
 * @author Zhi "George" Chen
 * @version April 2020
 */

import java.util.Random;

public class GoSmartAI extends GameComputerPlayer {
    // Instance Variables
    private float pass; // Chance to pass
    private int[][] memory = new int[13][13];
    private float rng;
    private int mypeice; // The color of the player

    /**
     * constructor
     *
     * @param name the computer player's name (e.g., "John")
     */
    public GoSmartAI(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        // Creates Instances
        int[][] board;
        mypeice = 0;

        // Accepts info
        if (info instanceof GoState) {
            board = ((GoState) info).getBoard(); // Local understanding of board
        } else {
            return;
        }

        pass += 0.01;

        // Creates Valuemap
        float[][] value = valuemap(board);

        // Creates Choicemap
        float[][] choice = new float[board.length][board.length];

        // Runs algorithms
        apply_rng(choice);
        //apply_bias(choice);
        apply_capture(choice, value, board);
        apply_protect(choice, value, board);
        apply_occupied(choice, board, memory);

        // Makes move
        choose(choice);
    }

    /** random: chooses a random position on the board
     *
     * @param input board of the gameState
     */
    private float[][] valuemap(int[][] input) {
        float[][] output = new float[input.length][input.length];
        int total = 0;
        for (int x1 = 0; x1 < input.length; x1++) {
            for (int y1 = 0; y1 < input.length; y1++) {
                if (input[x1][y1] != 0) {
                    total ++;
                }
                output[x1][y1] = 0;
            }
        }

        for (int x1 = 0; x1 < input.length; x1++) {
            for (int y1 = 0; y1 < input.length; y1++) {
                for (int x2 = 0; x2 < input.length; x2++) {
                    for (int y2 = 0; y2 < input.length; y2++) {
                        float effect = (float)((Math.abs(y2 - y1) + Math.abs(x2 - x1)));
                        if (input[x2][y2] == mypeice) {
                            output[x1][y1] += effect;
                        } else if (input[x2][y2] != 0){
                            output[x1][y1] -= effect;
                        }
                    }
                }
            }
        }
        return output;
    }

    /** choose: chooses the highest value position on the board
     *
     * @param input board of the gameState
     */
    private void choose(float[][] input) {
        Log.d("SmartAI","Choosing...");
        float max = -100000;
        int x_max = -1;
        int y_max = -1;

        // Finds the highest value
        for (int x = 0; x < input.length; x ++) {
            for (int y = 0; y < input.length; y ++) {
                if (input[x][y] > max) {
                    max = input[x][y];
                    x_max = x;
                    y_max = y;
                }
            }
        }
        Log.d("SmartAI","Max: " + max);
        Log.d("SmartAI","x:" + x_max);
        Log.d("SmartAI","y:" + y_max);
        if (max > pass) {
            // Updates Move History
            memory[x_max][y_max] = 1;

            // Sends GameAction
            game.sendAction(new GoMoveAction(this, x_max, y_max));
        } else {
            Log.d("SmartAI","Pass");
            game.sendAction(new GoPassAction(this));
        }
    }

    private void apply_rng(float[][] choice) {
        Random rand = new Random();
        for (int x = 0; x < choice.length; x ++) {
            for (int y = 0; y < choice.length; y ++) {
                choice[x][y] += (float)rand.nextInt(100) / 200;
            }
        }
    }

    private void apply_bias(float[][] choice) {
        for (int x = 0; x < choice.length; x ++) {
            for (int y = 0; y < choice.length; y ++) {
                choice[x][y] += (choice.length - Math.abs((choice.length / 2) + 0.5 - x) - Math.abs((choice.length / 2) + 0.5 - y)) / 300;
            }
        }
    }

    private void apply_occupied(float[][] choice, int[][] board, int[][] memory) {
        for (int x = 0; x < choice.length; x ++) {
            for (int y = 0; y < choice.length; y ++) {
                if (board[x][y] != 2 || memory[x][y] == 1) {
                    choice[x][y] = -1000000;
                }
            }
        }
    }

    private void apply_capture(float[][] choice, float[][] value, int[][] board) {
        for (int x = 0; x < choice.length; x ++) {
            for (int y = 0; y < choice.length; y ++) {
                if (board[x][y] == 1) {
                    if (x != 0) {
                        choice[x-1][y] += 2;
                    }
                    if (x != choice.length - 1) {
                        choice[x+1][y] += 2;
                    }
                    if (y != 0) {
                        choice[x][y-1] += 2;
                    }
                    if (y != choice.length - 1) {
                        choice[x][y+1] += 2;
                    }
                }
            }
        }
    }

    private void apply_protect(float[][] choice, float[][] value, int[][] board) {
        for (int x = 0; x < choice.length; x ++) {
            for (int y = 0; y < choice.length; y ++) {
                if (board[x][y] == 0) {
                    if (x != 0) {
                        choice[x-1][y] += 1;
                    }
                    if (x != choice.length - 1) {
                        choice[x+1][y] += 1;
                    }
                    if (y != 0) {
                        choice[x][y-1] += 1;
                    }
                    if (y != choice.length - 1) {
                        choice[x][y+1] += 1;
                    }
                }
            }
        }
    }
}
