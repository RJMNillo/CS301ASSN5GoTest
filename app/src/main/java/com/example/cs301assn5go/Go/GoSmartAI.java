package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GameComputerPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;

public class GoSmartAI extends GameComputerPlayer {
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

    }
}
