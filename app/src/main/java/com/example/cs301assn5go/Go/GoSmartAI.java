package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GameComputerPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;

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
        // Accepts info
        int[][] board; // Local understanding of board
        pass += 0.02;
    }

    /** random: chooses a random position on the board
     *
     * @param input board of the gameState
     */
    private float[][] valuemap(float[][] input) {
        float[][] output = new float[input.length][input.length];
        int total = 0;
        for (int x1 = 0; x1 < input.length; x1++) {
            for (int y1 = 0; y1 < input.length; y1++) {
                if (input[x1][y1] != 0) {
                    total ++;
                }
            }
        }

        for (int x1 = 0; x1 < input.length; x1++) {
            for (int y1 = 0; y1 < input.length; y1++) {
                for (int x2 = 0; x2 < input.length; x2++) {
                    for (int y2 = 0; y2 < input.length; y2++) {
                        float effect = 1/((float)Math.abs(y2+x2-y1-x1)) / total;
                        if (input[x2][y2] == mypeice) {
                            output[x1][y1] = effect;
                        } else {
                            output[x1][y1] = -effect;
                        }
                    }
                }
            }
        }
        return output;
    }
}
