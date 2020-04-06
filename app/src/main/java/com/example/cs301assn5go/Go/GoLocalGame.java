package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GamePlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.LocalGame;
import com.example.cs301assn5go.game.GameFramework.GameFramework.actionMessage.GameAction;

import java.util.ArrayList;

/**
 * The GoLocalGame class for a simple Go game. Defines and enforces
 * the game rules; handles interactions with players.
 *
 * @author Braeden Lane
 * @version April 2020
 */
public class GoLocalGame extends LocalGame {
    //Tag for logging
    private static final String TAG = "GoLocalGame";
    // the game's state
    protected GoState state;

    // the number of moves that have been played so far
    protected int moveCount;

    // list of moves that have been played so far
    private ArrayList<GameAction> moveList;

    // boolean to track if passes are in effect, helps to end the game
    private boolean passInEffect;

    /**
     * Constructor for the GoLocalGame.
     */
    public GoLocalGame() {
        super();
        state = new GoState();
        moveCount = 0;
        moveList = new ArrayList<>();
        passInEffect = false;
    }

    /**
     * Check if the game is over. It is over, return a string that tells
     * who the winner(s), if any, are. If the game is not over, return null;
     *
     * @return
     * 		a message that tells who has won the game, or null if the
     * 		game is not over
     */
    @Override
    protected String checkIfGameOver() {
        //TODO
        int gameWinner = 0;
        return playerNames[gameWinner]+" is the winner.";
    }

    /**
     * Tell whether the given player is allowed to make a move at the
     * present point in the game.
     *
     * @param playerIdx
     * 		the player's player-number (ID)
     * @return
     * 		true iff the player is allowed to move
     */
    protected boolean canMove(int playerIdx) { return playerIdx == state.getTurn(); }

    /**
     * Notify the given player that its state has changed. This should involve sending
     * a GameInfo object to the player. If the game is not a perfect-information game
     * this method should remove any information from the game that the player is not
     * allowed to know.
     *
     * @param p
     * 			the player to notify
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p ) {
        //make a copy of the state, and send it to the player
        p.sendInfo(new GoState(state));
    }

    /**
     * Makes a move on behalf of a player.
     *
     * @param action
     * 			The move that the player has sent to the game
     * @return
     * 			Tells whether the move was a legal one.
     */
    @Override
    protected boolean makeMove(GameAction action) {
        if(action instanceof GoMoveAction) {
            // get the row and the column position of the player's move
            GoMoveAction gm = (GoMoveAction) action;
            int row = gm.getRow();
            int col = gm.getCol();

            // get the 0/1 id of our player
            int playerId = getPlayerIdx(gm.getPlayer());

            // if that space is not legal
            if(state.getPiece(row, col) != 2) { // TODO **********FOR NOW THIS JUST CHECKS IF THE SPOT IS EMPTY
                return false;
            }

            // get the 0/1 id of the player whose move it is
            int whoseMove = state.getTurn();

            // place the player's piece on the selected square
            state.setBoard(row, col, playerId);

            // make it the other player's turn
            state.setTurn(1-whoseMove);

            // bump the move count
            moveCount++;

            // add the move to the move list
            moveList.add(action);

            // return true, indicating it was a legal move
            return true;
        } else {
            if(action instanceof GoPassAction) {
                if(passInEffect) {
                    checkIfGameOver();
                    return true;
                } else {
                    // set passInEffect to true
                    passInEffect = true;

                    // return true, indicating it was a legal move
                    return true;
                }
            }
        }
        // then both types of moves failed
        return false;
    }
}
