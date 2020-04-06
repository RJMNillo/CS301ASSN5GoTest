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
    }

    /**
     * Getter Methods
     * Get a certain variable from GoState.
     */
    
}
