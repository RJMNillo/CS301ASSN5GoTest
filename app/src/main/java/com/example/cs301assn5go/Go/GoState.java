package com.example.cs301assn5go.Go;

import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameState;

/**
 * The state of the game of Go
 * @author Reggie Jan Marc Nillo
 */

public class GoState extends GameState
{
    //Instance Variables
    private double player0score;
    private double player1score;
    private int playerTurn;
    private int player0captures;
    private int player1captures;
    private int[][] board;

    /**
     * Constructor: Creates an instance of the GameState.
     * Requires no Parameters.
     */
    public GoState()
    {
            player0score = 0;
            player1score = 0;
            playerTurn = 0;
            player0captures = 0;
            player1captures = 0;
    }

    /**
     * Copy Constructor: Creates a copy of the instance of the GameState.
     * Requires the aforementioned state.
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

    /**
     * Getter Methods
     * Get a certain variable from GoState.
     */
    public double getPlayer0score()
    {
        return player0score;
    }

    public double getPlayer1score()
    {
        return player1score;
    }

    public int getTurn()
    {
        return playerTurn;
    }

    public int getPlayer0captures()
    {
        return player0captures;
    }

    public int getPlayer1captures()
    {
        return player1captures;
    }

    public int[][] getBoard() {
        return board;
    }

    /**
     * Setter Methods
     */
    public void setPlayer0score(double scoreinc)
    {
        player0score = scoreinc;
    }
    public void setPlayer1score(double scoreinc)
    {
        player1score = scoreinc;
    }
    public void setTurn(int nextTurn)
    {
        playerTurn = nextTurn;
    }
    public void setPlayer0captures(int addcapture)
    {
        player0captures = addcapture;
    }
    public void setPlayer1captures(int addcapture)
    {
        player1captures = addcapture;
    }

    public int getPiece(int row, int col)
    {
        return board[row][col];
    }

    public void setBoard(int r, int c, int playerID)
    {
        board[r][c] = playerID;
    }
}
