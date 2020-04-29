package com.example.cs301assn5go.Go;

import android.util.Log;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GameComputerPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.NotYourTurnInfo;

import java.util.Random;

/**
 * An AI to be used in Go that makes somewhat random moves
 *
 * @author Zhi "George" Chen
 * @version April 2020
 */
public class GoDumbAI extends GameComputerPlayer {
    // Instance Variables
    private float pass; // Chance to pass

    /**
     * Default Constructor
     *
     * @param name name of the AI
     */
    public GoDumbAI(String name) {
        super(name);
    }

    /**
     * receiveInfo overriden method called upon request for a move
     *
     * @param info information or a copy of the gameState
     */
    @Override
    protected void receiveInfo(GameInfo info) {
        // Makes sure the info is the gameState
        if (info instanceof NotYourTurnInfo) return;
        if (info instanceof IllegalMoveInfo) return;

        // Accepts info
        int[][] board = ((GoState) info).getBoard(); // Local understanding of board

        // Increments chance of passing every call
        pass += 0.001;

        // Chooses a random move
        if (pass >= 1) {
            // Sends pass gameAction
            game.sendAction(new GoPassAction(this));
        } else {
            // Sends a random move as a gameAction
            int[] choice;
            choice = random(board);
            int x = choice[0];
            int y = choice[1];
            Log.d("DumbAI","Move: " + x + "," + y);
            game.sendAction(new GoMoveAction(this, x, y));
        }
    }

    /** random: chooses a random position on the board
     *
     * @param board the board associated with the copied gameState
     */
    private int[] random(int[][] board) {
        // Generates a random value based of the size of the board
        int size = board.length;
        Random rand = new Random();
        int num = rand.nextInt(size*size);

        // Iterates through the board for that amount of times, looping if necessary
        while (num >= 0) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (board[x][y] == 2) {
                        // Increment
                        num--;

                        // Once 0 is reached, send the current cell as the move
                        if (num == 0) {
                            int[] info = new int[2];
                            info[0] = x;
                            info[1] = y;
                            return info;
                        }
                    }
                }
            }
        }

        // Error catch
        return new int[]{-1,-1};
    }
}
