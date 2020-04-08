package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GameComputerPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;

import java.util.Random;

public class GoDumbAI extends GameComputerPlayer {
    // Instance Variables
    float pass; // Chance to pass
    int[][] board; // Local understanding of board

    // Default Constructor
    public GoDumbAI(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        // Accepts info
        board = ((GoState)info).getBoard();
        pass += 0.02;

        // Chooses a random move
        if (pass >= 1) {
            game.sendAction(new GoPassAction(this));
        } else {
            int[] choice = random(board);
            int x = choice[0];
            int y = choice[1];
            game.sendAction(new GoMoveAction(this, x, y));
        }
    }

    /** random: chooses a random position on the board
     *
     * @param board the current game
     */
    private int[] random(int[][] board) {
        int size = board.length;
        Random rand = new Random();
        int num = rand.nextInt(size*size);

        for (int x = 0; x < size; x++ ) {
            for (int y = 0; y < size; y++) {
                if (board[x][y] == 0) {
                    num --;
                    if (num == 0) {
                        int[] info = {x,y};
                        return info;
                    }
                }
            }
        }
        return null;
    }
}
