package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GameComputerPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.NotYourTurnInfo;

import java.util.Random;

public class GoDumbAI extends GameComputerPlayer {
    // Instance Variables
    private float pass; // Chance to pass

    // Default Constructor
    public GoDumbAI(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        if (info instanceof NotYourTurnInfo) return;
        // Accepts info
            //int[][] board = ((GoState) info).getBoard(); // Local understanding of board
            pass += 0.02;

        // Chooses a random move
        if (pass >= 1) {
            game.sendAction(new GoPassAction(this));
        } else {
            int[] choice;
            choice = random();
            int x = choice[0];
            int y = choice[1];
            game.sendAction(new GoMoveAction(this, x, y));
        }
    }

    /** random: chooses a random position on the board
     *
     */
    private int[] random() {
        int size = 13;
        Random rand = new Random();
        int x = rand.nextInt(size);
        int y = rand.nextInt(size);

        int[] info = new int[2];
        info[0] = x;
        info[1] = y;
        return info;

//        for (int x = 0; x < size; x++ ) {
//            for (int y = 0; y < size; y++) {
//                if (board[x][y] == 2) {
//                    num --;
//                    if (num == 0) {
//                        int[] info = new int[2];
//                        info[0] = x;
//                        info[1] = y;
//                        return info;
//                    }
//                }
//            }
//        }
//        return new int[]{-1,-1};
    }
}
