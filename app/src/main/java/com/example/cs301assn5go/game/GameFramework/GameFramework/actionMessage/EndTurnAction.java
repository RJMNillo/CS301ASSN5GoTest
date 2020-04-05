package com.example.cs301assn5go.game.GameFramework.GameFramework.actionMessage;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GamePlayer;

import java.io.Serializable;


public class EndTurnAction extends GameAction implements Serializable {
    //Tag for logging
    private static final String TAG = "EndTurnAction";

    //Long for network play - changed the number before the L to a 6 instead of a 7.
    private static final long serialVersionUID = 3067264564645016L;

    public EndTurnAction(GamePlayer player){
        super(player);
    }
}
