package com.example.cs301assn5go.Go;

import android.graphics.Point;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GamePlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.LocalGame;
import com.example.cs301assn5go.game.GameFramework.GameFramework.actionMessage.GameAction;

import java.util.ArrayList;

/**
 * The GoLocalGame class for a simple Go game. Defines and enforces
 * the game rules; handles interactions with players.
 *
 * @author Braeden Lane, Vandan Bhargava
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
    private boolean passInEffect1;
    private boolean passInEffect2;

    private ArrayList<Point> toBeDeleted = new ArrayList<Point>();

    /**
     * Constructor for the GoLocalGame.
     */
    public GoLocalGame() {
        super();
        state = new GoState();
        moveCount = 0;
        moveList = new ArrayList<>();
        passInEffect1 = false;
        passInEffect2 = false;
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
        // This function is called if the player passes
        if (passInEffect2) {
            int gameWinner = 0;
            if (score(0) < score(1)) {
                gameWinner = 1;
            }
            return playerNames[gameWinner] + " is the winner.";
        }
        return null;
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
        int toPlayerID = getPlayerIdx(p);
        int playerID;
        if(toPlayerID == 1){
            playerID = 0;
        } else {
            playerID = 1;
        }
        int[][] board = state.getBoard();
        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board.length; j++){
                if(board[i][j] == playerID+4){
                    state.setBoard(i,j,2);
                }
            }
        }
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
            int [][] board = state.getBoard();

            // get the 0/1 id of our player
            int playerId = getPlayerIdx(gm.getPlayer());

            // if that space is not legal
            if(state.getPiece(row, col) == 1 || state.getPiece(row,col) == 0) { // TODO **********FOR NOW THIS JUST CHECKS IF THE SPOT IS EMPTY
                return false;
            }
            int up;
            int down;
            int left;
            int right;
            try {
                up = board[row - 1][col];
            } catch(ArrayIndexOutOfBoundsException e) {
                up = 3;
            }
            try {
                down = board[row + 1][col];
            } catch(ArrayIndexOutOfBoundsException e) {
                down = 3;
            }
            try {
                left = board[row][col - 1];
            } catch(ArrayIndexOutOfBoundsException e) {
                left = 3;
            }
            try {
                right = board[row][col + 1];
            } catch(ArrayIndexOutOfBoundsException e) {
                right = 3;
            }
            if (up != 2 && down != 2 && left != 2 && right != 2) {
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

            //clears captured pieces if any
            checkIfCaptured(row,col);

            // reset passInEffect
            passInEffect1 = false;

            // return true, indicating it was a legal move
            return true;
        } else {
            if(action instanceof GoPassAction) {
                if(passInEffect1) {
                    passInEffect2 = true;
                }
                passInEffect1 = true;
                // get the 0/1 id of the player whose move it is
                int whoseMove = state.getTurn();

                // make it the other player's turn
                state.setTurn(1-whoseMove);

                // bump the move count
                moveCount++;

                // add the move to the move list
                moveList.add(action);
                return true;


                // return true, indicating it was a legal move
                }
            }
        // then both types of moves failed
        return false;
    }

    /**
     * Checks if the stone that has just been placed captured anything around it using helper method
     * checkIfLocked that can follow the trail of the entire group of stones being captured. If it
     * is captured then removes it.
     *
     * @param row
     * @param col
     *          location of the stone that has just been placed.
     */
    private void checkIfCaptured(int row, int col){
        int[][] realBoard = state.getBoard();
        int[][] board = realBoard;
        int i = row;
        int j = col;
        if(i == 0 && j == 0){
            if(board[i][j] == 0){
                if(board[i+1][j] == 1){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j+1] == 1){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i+1][j] == 0){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j+1] == 0){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer1Captures();
                    }
                }
            }
        } else if(i == board.length-1 && j == board.length-1){
            if(board[i][j] == 0){
                if(board[i-1][j] == 1){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j-1] == 1){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i-1][j] == 0){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j-1] == 0){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer1Captures();
                    }
                }
            }
        } else if(i == 0 && j == board.length-1){
            if(board[i][j] == 0){
                if(board[i+1][j] == 1){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j-1] == 1){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i+1][j] == 0){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j-1] == 0){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer1Captures();
                    }
                }
            }
        } else if(i == board.length-1 && j == 0){
            if(board[i][j] == 0){
                if(board[i-1][j] == 1){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j+1] == 1){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i+1][j] == 0){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j+1] == 0){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer1Captures();
                    }
                }
            }
        } else if(i == 0 && j < board.length-1){
            if(board[i][j] == 0){
                if(board[i+1][j] == 1){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j+1] == 1){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j-1] == 1){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i+1][j] == 0){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j+1] == 0){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j-1] == 0){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer1Captures();
                    }
                }
            }
        } else if(i == board.length - 1 && j < board.length-1){
            if(board[i][j] == 0){
                if(board[i-1][j] == 1){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j+1] == 1){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j-1] == 1){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i-1][j] == 0){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j+1] == 0){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j-1] == 0){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer1Captures();
                    }
                }
            }
        } else if(j == 0 && i < board.length-1){
            if(board[i][j] == 0){
                if(board[i-1][j] == 1){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i+1][j] == 1){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j+1] == 1){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i-1][j] == 0){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i+1][j] == 0){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j+1] == 0){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer1Captures();
                    }
                }
            }
        } else if(j == board.length - 1 && i < board.length-1){
            if(board[i][j] == 0){
                if(board[i-1][j] == 1){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i+1][j] == 1){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j-1] == 1){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i-1][j] == 0){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i+1][j] == 0){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j-1] == 0){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer1Captures();
                    }
                }
            }
        } else {
            if(board[i][j] == 0){
                if(board[i-1][j] == 1){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i+1][j] == 1){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j-1] == 1){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer0Captures();
                    }
                }
                if(board[i][j+1] == 1){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            } else if(board[i][j] == 1){
                if(board[i-1][j] == 0){
                    if(checkIfLocked(i-1, j, realBoard)){
                        realBoard[i-1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i+1][j] == 0){
                    if(checkIfLocked(i+1, j, realBoard)){
                        realBoard[i+1][j] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j-1] == 0){
                    if(checkIfLocked(i, j-1, realBoard)){
                        realBoard[i][j-1] = 2;
                        state.addPlayer1Captures();
                    }
                }
                if(board[i][j+1] == 1){
                    if(checkIfLocked(i, j+1, realBoard)){
                        realBoard[i][j+1] = 2;
                        state.addPlayer0Captures();
                    }
                }
            }
        }
        for(int k = 0; k<toBeDeleted.size(); k++){
            realBoard[toBeDeleted.get(k).x][toBeDeleted.get(k).y] = 2;
        }
        toBeDeleted = new ArrayList<Point>();
    }

    /**
     * Helper method for the checkIfCaptured method. Through recursion, it goes through the baord
     * checks for liberties and then subtracts them from each stone. It uses the given board, so as
     * to no confuse multiple points. It also adds all locked stones into an array list so that all
     * the stones that are locked can be deleted
     *
     * @param row
     * @param col
     *          location of the stone to be checked
     * @param givenBoard
     *          Gives itself the updated board so it doesn't get confused with the real board
     * @return
     *          Returns true if the stone(s) is locked, returns false if it is not
     */
    private boolean checkIfLocked(int row, int col, int[][] givenBoard){
        int[][] realBoard = givenBoard;
        int[][] board = new int[realBoard.length][realBoard.length];
        ArrayList<Point> potentialToBeDeleted = new ArrayList<Point>();
        for(int i = 0; i<realBoard.length; i++){
            for(int j = 0; j<realBoard.length; j++){
                board[i][j] = realBoard[i][j];
            }
        }
        if(row >= board.length || col >= board.length){
            return false;
        }
        int i = row;
        int j = col;
        int colorCheck = realBoard[row][col];
        board[i][j] = 8;
        boolean checker = false;
        int liberties = 10;
        if(i == 0 && j == 0){
            liberties = 2;
            if(board[i+1][j] == 2){
                liberties = 10;
            } else if(board[i+1][j] == colorCheck){
                if(checkIfLocked(i+1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i+1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j+1] == 2){
                liberties = 10;
            } else if(board[i][j+1] == colorCheck){
                if(checkIfLocked(i, j+1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j+1));
                }
            } else {
                liberties--;
            }
        } else if(i == 0 && j == board.length-1){
            liberties = 2;
            if(board[i+1][j] == 2){
                liberties = 10;
            } else if(board[i+1][j] == colorCheck){
                if(checkIfLocked(i+1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i+1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j-1] == 2){
                liberties = 10;
            } else if(board[i][j-1] == colorCheck){
                if(checkIfLocked(i, j-1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j-1));
                }
            } else {
                liberties--;
            }
        } else if(i == board.length-1 && j == board.length-1){
            liberties = 2;
            if(board[i-1][j] == 2){
                liberties=10;
            } else if(board[i-1][j] == colorCheck){
                if (checkIfLocked(i - 1, j, board)) {
                    liberties--;
                    potentialToBeDeleted.add(new Point(i-1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j-1] == 2){
                liberties = 10;
            } else if(board[i][j-1] == colorCheck){
                if(checkIfLocked(i, j-1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j-1));
                }
            } else {
                liberties--;
            }
        } else if(i == board.length-1 && j == 0){
            liberties = 2;
            if(board[i-1][j] == 2){
                liberties = 10;
            } else if(board[i-1][j] == colorCheck){
                if(checkIfLocked(i-1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i-1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j+1] == 2){
                liberties = 10;
            } else if(board[i][j+1] == colorCheck){
                if(checkIfLocked(i, j+1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j+1));
                }
            } else {
                liberties--;
            }
        } else if(i == 0 && j < board.length-1){
            liberties = 3;
            if(board[i+1][j] == 2){
                liberties = 10;
            } else if(board[i+1][j] == colorCheck){
                if(checkIfLocked(i+1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i+1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j-1] == 2){
                liberties = 10;
            } else if(board[i][j-1] == colorCheck){
                if(checkIfLocked(i, j-1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j-1));
                }
            } else {
                liberties--;
            }
            if(board[i][j+1] == 2){
                liberties = 10;
            } else if(board[i][j+1] == colorCheck){
                if(checkIfLocked(i, j+1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j+1));
                }
            } else {
                liberties--;
            }
        } else if(i < board.length-1 && j == 0){
            liberties = 3;
            if(board[i+1][j] == 2){
                liberties=10;
            } else if(board[i+1][j] == colorCheck){
                if(checkIfLocked(i+1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i+1, j));
                }
            } else {
                liberties--;
            }
            if(board[i-1][j] == 2){
                liberties = 10;
            } else if(board[i-1][j] == colorCheck){
                if(checkIfLocked(i-1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i-1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j+1] == 2){
                liberties=10;
            } else if(board[i][j+1] == colorCheck){
                if(checkIfLocked(i,j+1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j+1));
                }
            } else {
                liberties--;
            }
        } else if(i < board.length-1 && j == board.length-1){
            liberties = 3;
            if(board[i+1][j] == 2){
                liberties = 10;
            } else if(board[i+1][j] == colorCheck){
                if(checkIfLocked(i+1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i+1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j-1] == 2){
                liberties = 10;
            } else if(board[i][j-1] == colorCheck){
                if(checkIfLocked(i,j-1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j-1));
                }
            } else {
                liberties--;
            }
            if(board[i-1][j] == 2){
                liberties=10;
            } else if(board[i-1][j] == colorCheck){
                if(checkIfLocked(i-1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i-1, j));
                }
            } else {
                liberties--;
            }
        } else if(i == board.length-1 && j < board.length-1){
            liberties = 3;
            if(board[i-1][j] == 2){
                liberties=10;
            } else if(board[i-1][j] == colorCheck){
                if(checkIfLocked(i-1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i-1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j-1] == 2){
                liberties=10;
            } else if(board[i][j-1] == colorCheck){
                if(checkIfLocked(i, j-1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j-1));
                }
            } else {
                liberties--;
            }
            if(board[i][j+1] == 2){
                liberties=10;
            } else if(board[i][j+1] == colorCheck){
                if(checkIfLocked(i, j+1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j+1));
                }
            } else {
                liberties--;
            }
        } else {
            liberties = 4;
            if(board[i+1][j] == 2){
                liberties=10;
            } else if(board[i+1][j] == colorCheck){
                if(checkIfLocked(i+1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i+1, j));
                }
            } else {
                liberties--;
            }
            if(board[i-1][j] == 2){
                liberties=10;
            } else if(board[i-1][j] == colorCheck){
                if(checkIfLocked(i-1, j, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i-1, j));
                }
            } else {
                liberties--;
            }
            if(board[i][j-1] == 2){
                liberties=10;
            } else if(board[i][j-1] == colorCheck){
                if(checkIfLocked(i, j-1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j-1));
                }
            } else {
                liberties--;
            }
            if(board[i][j+1] == 2){
                liberties=10;
            } else if(board[i][j+1] == colorCheck){
                if(checkIfLocked(i,j+1, board)){
                    liberties--;
                    potentialToBeDeleted.add(new Point(i, j+1));
                }
            } else {
                liberties--;
            }
        }
        if(liberties == 0){
            toBeDeleted.addAll(potentialToBeDeleted);
        }
        return (liberties == 0);
    }

    /**
     * clear_dead: removes dead pieces
     */
    private void clearDead(){
        //TODO
    }

    /**
     * score: counts up score of player
     *
     * @param player player id
     *
     * @return score of player
     */
    private int score(int player){
        int score = 0;
        for(int x = 0; x < state.getBoard().length; x ++) {
            for(int y = 0; y < state.getBoard().length; y ++) {
                if (player == state.getBoard()[x][y]) {
                    score ++;
                }
            }
        }

        // Counts up surrounded spaces TODO
        return score;
    }
}
