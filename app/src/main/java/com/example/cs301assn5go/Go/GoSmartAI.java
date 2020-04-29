package com.example.cs301assn5go.Go;

import android.util.Log;

import com.example.cs301assn5go.R;
import com.example.cs301assn5go.game.GameFramework.GameFramework.GameComputerPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;

/**
 * An AI to be used in Go that makes calculated and "smart" decisions
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

    /**
     * receiveInfo accepts info or a copy of the gameState
     *
     * @param info the info or copy of the gameState
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        // Creates temporary instances
        int[][] board;
        mypeice = 0;

        // Accepts info
        if (info instanceof GoState) {
            board = ((GoState) info).getBoard(); // Local understanding of board
        } else {
            return;
        }

        // Creates Valuemap
        float[][] value = valuemap(board);

        // Creates Choicemap
        float[][] choice = new float[board.length][board.length];

        // Runs algorithms
        apply_rng(choice);
        apply_bias(choice);
        apply_capture(choice, value, board);
        apply_protect(choice, value, board);
        apply_occupied(choice, board, memory);

        // Makes move
        choose(choice);
    }

    /**
     * valuemap: chooses a random position on the board
     *
     * @param input board of the gameState
     * @return a map of an arbitrary relative value of tiles depending on nearby pieces.
     */
    private float[][] valuemap(int[][] input) {
        float[][] output = new float[input.length][input.length];
        int total = 0;
        for (int x1 = 0; x1 < input.length; x1++) {
            for (int y1 = 0; y1 < input.length; y1++) {
                if (input[x1][y1] != 0) {
                    total++;
                }
                output[x1][y1] = 0;
            }
        }

        for (int x = 0; x < input.length; x++) {
            for (int y = 0; y < input.length; y++) {
                output[y][x] = 1;
                if (x != 0 && input[y][x - 1] == 0)
                    output[y][x]++;
                if (x != input.length - 1 && input[y][x + 1] == 0)
                    output[y][x]++;
                if (y != 0 && input[y - 1][x] == 0)
                    output[y][x]++;
                if (y != input.length - 1 && input[y + 1][x] == 0)
                    output[y][x]++;
                if (x == 0 || x == input.length - 1)
                    output[y][x]--;
                if (y == 0 || y == input.length - 1)
                    output[y][x]--;
            }
        }
        return output;
    }

    /**
     * choose: chooses the highest value position on the board
     *
     * @param input board of the gameState
     */
    private void choose(float[][] input) {
        Log.d("SmartAI", "Choosing...");
        float max = -100000;
        int x_max = -1;
        int y_max = -1;

        // Finds the highest value
        for (int x = 0; x < input.length; x++) {
            for (int y = 0; y < input.length; y++) {
                if (input[x][y] > max) {
                    max = input[x][y];
                    x_max = x;
                    y_max = y;
                }
            }
        }

        Log.d("SmartAI", "Max: " + max);
        Log.d("SmartAI", "x:" + x_max);
        Log.d("SmartAI", "y:" + y_max);

        // Decides whether to pass
        if (max > pass) {
            // Updates Move History
            memory[x_max][y_max] = 1;

            // Sends GameAction
            game.sendAction(new GoMoveAction(this, x_max, y_max));
        } else {
            Log.d("SmartAI", "Pass");
            game.sendAction(new GoPassAction(this));
        }
    }

    /**
     * apply_rng applies a low rng to solve ties
     *
     * @param choice the final map to modify
     */
    private void apply_rng(float[][] choice) {
        Random rand = new Random();
        for (int x = 0; x < choice.length; x++) {
            for (int y = 0; y < choice.length; y++) {
                // Generates a random value
                choice[x][y] += (float) rand.nextInt(100) / 200;
            }
        }
    }

    /**
     * apply_bias applies a low bias towards center of map
     *
     * @param choice the final map to modify
     */
    private void apply_bias(float[][] choice) {
        for (int x = 0; x < choice.length; x++) {
            for (int y = 0; y < choice.length; y++) {
                // Generates value based on distance to center
                choice[x][y] += (choice.length - Math.abs((choice.length / 2) + 0.5 - x) - Math.abs((choice.length / 2) + 0.5 - y)) / choice.length / 10;
            }
        }
    }

    /**
     * apply_occupied checks if a position is occupied or has been already tried
     *
     * @param choice the final map to modify
     * @param board the game board
     * @param memory the history of played moves
     */
    private void apply_occupied(float[][] choice, int[][] board, int[][] memory) {
        for (int x = 0; x < choice.length; x++) {
            for (int y = 0; y < choice.length; y++) {
                // Checks whether this position has been tried, or a piece already exists
                if (board[x][y] != 2 || memory[x][y] == 1) {
                    choice[x][y] = -1000000;
                }
            }
        }
    }

    /**
     * apply_capture attempts to capture pieces
     *
     * @param choice the final map to modify
     * @param value the map of importance of each tile
     * @param board the game board
     */
    private void apply_capture(float[][] choice, float[][] value, int[][] board) {
        // Int for calculating freedoms
        int freedom = 0;

        // Iterates through board
        for (int x = 0; x < choice.length; x++) {
            for (int y = 0; y < choice.length; y++) {

                // Calculates freedoms
                if (board[y][x] == 1) {
                    freedom = 4;
                    if (x != 0 && board[y][x - 1] == 0)
                        freedom--;
                    if (x != choice.length - 1 && board[y][x + 1] == 0)
                        freedom--;
                    if (y != 0 && board[y - 1][x] == 0)
                        freedom--;
                    if (y != choice.length - 1 && board[y + 1][x] == 0)
                        freedom--;
                    if (x == 0 || x == choice.length - 1)
                        freedom --;
                    if (y == 0 || y == choice.length - 1)
                        freedom --;

                    // Forces AI to be proactive
                    if (freedom > 1)
                        freedom --;

                    // Applies weights based on freedom and distance
                    int row;
                    for (int col = 0; col < freedom - 1; col++) {
                        row = freedom - col;
                        if (y + row < board.length && x + col < board.length)
                            choice[y + row][x + col] += (4 - freedom) * (4 - freedom) + value[y][x];
                        if (y - row > 0 && x + col < board.length)
                            choice[y - row][x + col] += (4 - freedom) * (4 - freedom) + value[y][x];
                        if (y + row < board.length && x - col > 0)
                            choice[y + row][x - col] += (4 - freedom) * (4 - freedom) + value[y][x];
                        if (y - row > 0 && x - col > 0)
                            choice[y - row][x - col] += (4 - freedom) * (4 - freedom) + value[y][x];
                    }
                }
            }
        }
    }

    /**
     * apply_capture attempts to capture pieces
     *
     * @param choice the final map to modify
     * @param value the map of importance of each tile
     * @param board the game board
     */
    private void apply_protect(float[][] choice, float[][] value, int[][] board) {
        // Int for calculating freedoms
        int freedom = 0;

        // Iterates through board
        for (int x = 0; x < choice.length; x++) {
            for (int y = 0; y < choice.length; y++) {

                // Calculates freedoms
                if (board[y][x] == 0) {
                    freedom = 4;
                    if (x != 0 && board[y][x - 1] == 1)
                        freedom--;
                    if (x != choice.length - 1 && board[y][x + 1] == 1)
                        freedom--;
                    if (y != 0 && board[y - 1][x] == 1)
                        freedom--;
                    if (y != choice.length - 1 && board[y + 1][x] == 1)
                        freedom--;
                    if (x == 0 || x == choice.length - 1)
                        freedom --;
                    if (y == 0 || y == choice.length - 1)
                        freedom --;

                    // Forces AI to be proactive
                    if (freedom > 1)
                        freedom --;

                    // Applies weights based on freedom and distance
                    int row;
                    for (int col = 0; col < freedom - 1; col++) {
                        row = freedom - col;
                        if (y + row < board.length && x + col < board.length)
                            choice[y + row][x + col] += 2 * ((4 - freedom) * (4 - freedom) + value[y][x]);
                        if (y - row > 0 && x + col < board.length)
                            choice[y - row][x + col] += 2 * ((4 - freedom) * (4 - freedom) + value[y][x]);
                        if (y + row < board.length && x - col > 0)
                            choice[y + row][x - col] += 2 * ((4 - freedom) * (4 - freedom) + value[y][x]);
                        if (y - row > 0 && x - col > 0)
                            choice[y - row][x - col] += 2 * ((4 - freedom) * (4 - freedom) + value[y][x]);
                    }
                }
            }
        }
    }
}