package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameState;

/**
 * The state of the game of Go
 *
 * @author Reggie Jan Marc Nillo
 * @version April 2020
 */

public class GoState extends GameState
{
    //Instance Variables
    /** Score of player 0*/
    private double player0score;

    /** Score of player 1*/
    private double player1score;

    /** Whose turn it is */
    private int playerTurn;

    /** Player 1 Stones Player 0 captured */
    private int player0captures;

    /** Player 0 Stones Player 1 captured */
    private int player1captures;

    /** The board itself */
    private int[][] board;

    //methods
    /**
     * Constructor: Creates an instance of the GameState.
     *
     */
    public GoState()
    {
            player0score = 0;
            player1score = 0;
            playerTurn = 0;
            player0captures = 0;
            player1captures = 0;
            board = new int[13][13];
            for(int i = 0; i < 13; i++) {
                for(int j = 0; j < 13; j++) {
                    board[i][j] = 2;
                }
            }
    }

    /**
     * Copy Constructor: Creates a copy of the instance of the GameState.
     * @param CurrentState State of the game that is being copied
     */
    public GoState(GoState CurrentState)
    {
        player0score = CurrentState.player0score;
        player1score = CurrentState.player1score;
        playerTurn = CurrentState.playerTurn;
        player0captures = CurrentState.player0captures;
        player1captures = CurrentState.player1captures;
        board = CurrentState.board;
    }

   //Getter methods
    /**
     * Returns the score of player 0.
     * @return player 0's score from the state.
     */
    public double getPlayer0score()
    {
        return player0score;
    }

    /**
     * Returns the score of player 1.
     * @return player 1's score from the state.
     */
    public double getPlayer1score()
    {
        return player1score;
    }

    /**
     * Returns the Turn in the game.
     * @return the turn.
     */
    public int getTurn()
    {
        return playerTurn;
    }

    /**
     * Returns the captures of player 0.
     * @return player 0's score from the state.
     */
    public int getPlayer0captures()
    {
        return player0captures;
    }

    /**
     * Returns the score of player 1.
     * @return player 1's score from the state.
     */
    public int getPlayer1captures()
    {
        return player1captures;
    }

    /**
     * Grabs the board from the state.
     * @return the board of this state..
     */
    public int[][] getBoard()
    {
        return board;
    }

    //Setter Methods

    /**
     * Changes score for player 0.
     * @param scoreinc The increment in which the score will increase by.
     */
    public void setPlayer0score(double scoreinc)
    {
        player0score = scoreinc;
    }

    /**
     * Changes score for player 1.
     * @param scoreinc The increment in which the score will increase by.
     */
    public void setPlayer1score(double scoreinc)
    {
        player1score = scoreinc;
    }

    /**
     * Changes turn for the game.
     * @param nextTurn The increment in which the score will increase by.
     */
    public void setTurn(int nextTurn)
    {
        playerTurn = nextTurn;
    }

    /**
     * Changes the captures of Player 0
     * @param addcapture The increment for captures
     */
    public void setPlayer0captures(int addcapture)
    {
        player0captures = addcapture;
    }

    /**
     * Changes the captures of Player 1
     * @param addcapture The increment for captures
     */
    public void setPlayer1captures(int addcapture)
    {
        player1captures = addcapture;
    }

    /**
     * Increments the captures of Player 0 by 1.
     */
    public void addPlayer0Captures(){
        player0captures++;
    }

    /**
     * Increments the captures of Player 1 by 1.
     */
    public void addPlayer1Captures(){
        player1captures++;
    }

    /**
     * Grab a piece from the board for analysis.
     * @param row Row of the board
     * @param col Column of the board
     * @return a piece with said row and column of the board
     */
    public int getPiece(int row, int col)
    {
        return board[row][col];
    }

    /**
     * Place or remove a stone from the board.
     * @param r Row of the board
     * @param c Column of the board
     * @param playerID the stone being played IE who made the move
     */
    public void setBoard(int r, int c, int playerID)
    {
        board[r][c] = playerID;
    }
}
