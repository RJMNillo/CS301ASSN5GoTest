package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GamePlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.actionMessage.GameAction;

/**
 * A game-move object that a Go player sends to the game to make a move.
 */
public class GoMoveAction extends GameAction {

    // instance variables: the selected row and column
    private int row;
    private int col;

    /**
     * Constructor for GoMoveAction
     *
     * @param player the player making the move
     * @param row the row of the intersection selected
     * @param col the column of the intersection selected
     */
    public GoMoveAction(GamePlayer player, int row, int col) {
        super(player);
        this.row = row;
        this.col = col;
    }

    /**
     *  get the object's row
     *
     * @return the row selected
     */
    public int getRow() { return row; }

    /**
     * get the object's column
     *
     * @return the column selected
     */
    public int getCol() { return col; }

}
