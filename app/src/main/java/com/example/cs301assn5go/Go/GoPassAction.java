package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GamePlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.actionMessage.GameAction;

/**
 * A move-pass object that a Go player sends to the game to make a pass.
 *
 * @author Braeden Lane
 * @version April 2020
 */
public class GoPassAction extends GameAction {
    /**
     * Constructor for GoPassAction
     *
     * @param player the player making the move
     */
    public GoPassAction(GamePlayer player) {
        super(player);
    }
}
