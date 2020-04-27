package com.example.cs301assn5go.Go;

import android.util.Log;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GameComputerPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;

/**
 * An AI to be used in Go that makes calculated and smart decisions
 *
 * @author Zhi "George" Chen
 * @version April 2020
 */

public class GoSmartAI extends GameComputerPlayer {
    // Instance Variables
    private float pass; // Chance to pass
    private int[][] memory;
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

        // Accepts info
        if (info instanceof GoState) {
            board = ((GoState) info).getBoard(); // Local understanding of board
        } else {
            return;
        }

        // Creates Valuemap
        float[][] value = valuemap(board);

        // Makes move
        choose(value);
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
                if (input[x1][y1] == 2) {
                    for (int x2 = 0; x2 < input.length; x2++) {
                        for (int y2 = 0; y2 < input.length; y2++) {
                            float effect = (float)(3.5 - Math.abs(y2 - y1) - Math.abs(x2 - x1));
                            if (input[x2][y2] == mypeice) {
                                output[x1][y1] -= effect;
                            } else {
                                output[x1][y1] += effect;
                            }
                        }
                    }
                } else {
                    output[x1][y1] = -100000;
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
            game.sendAction(new GoMoveAction(this, x_max, y_max));
        } else {
            Log.d("SmartAI","Pass");
            game.sendAction(new GoPassAction(this));
        }
    }
}
