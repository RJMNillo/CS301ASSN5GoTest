package com.example.cs301assn5go.Go;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cs301assn5go.R;
import com.example.cs301assn5go.game.GameFramework.GameFramework.GameHumanPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.GameMainActivity;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.IllegalMoveInfo;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.cs301assn5go.game.GameFramework.GameFramework.utilities.Logger;

import org.w3c.dom.Text;

/**
 * A GUI that allows a human to play Go. Moves are made by clicking
 * regions on a canvas and confirming with a button
 *
 * @author Braeden Lane, Vandan Bhargava, Reggie Jan Marc Nillo
 * @version April 2020
 */
public class GoHumanPlayer extends GameHumanPlayer implements View.OnTouchListener, View.OnClickListener {
    // Tag for logging
    private static final String TAG = "GoHumanPlayer";

    // the current activity
    private Activity myActivity;

    // the surface view
    private GoView surfaceView;

    // the state
    private GoState state;

    // point that is not yet confirmed
    private Point notPoint;

    // the ID for the layout to use
    private int layoutID;

    //Modified Text Widgets
    private TextView P0captures;
    private TextView CurrentTurn;
    private TextView P1captures;
    private Button ConfirmButton;
    private Button PassButton;


    /**
     * constructor
     *
     * @param name the name of the player
     */
    public GoHumanPlayer(String name) {
        super(name);
        notPoint = null;
    }

    @Override
    public void onClick(View v) {
        if(v == myActivity.findViewById(R.id.button)) {
            Logger.log("onClick", "pass sent");
            game.sendAction(new GoPassAction(this));
        } else if(v == myActivity.findViewById(R.id.button2)){
            Logger.log("onClick", "confirm was clicked");
            if(notPoint!=null){
                game.sendAction(new GoMoveAction(this, notPoint.x, notPoint.y));
                Logger.log("onClick", "was sent");
                notPoint = null;
                surfaceView.invalidate();
            } else {
                surfaceView.flash(Color.RED, 500);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() != MotionEvent.ACTION_UP) return true;

        float x = event.getX();
        float y = event.getY();
        Logger.log("onTouch", "x: " + x + " | y: " + y);
        Point p = surfaceView.mapPieces(x, y);
        Logger.log("on Touch", "Point x: " + p.x + " | Point y: " + p.y);

        if(p == null || p.x == state.getBoard().length || p.y == state.getBoard().length) {
            surfaceView.flash(Color.RED, 500);
        } else {
            //GoMoveAction action = new GoMoveAction(this, p.x, p.y);
            Logger.log("onTouch", "Human player sending GMA ...");
            //game.sendAction(action);
            notPoint = p;
            for(int i = 0; i<state.getBoard().length; i++){
                for(int j = 0; j<state.getBoard().length; j++){
                    if(state.getPiece(i, j) == playerNum+4){
                        state.setBoard(i, j, 2);
                    }
                }
            }
            if(state.getPiece(notPoint.x, notPoint.y) == 2){
                state.setBoard(notPoint.x, notPoint.y, playerNum+4);
            }
            surfaceView.invalidate();
        }

        return true;
    }

    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    @Override
    public void receiveInfo(GameInfo info) {

        if(surfaceView == null) return;

        if(info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            surfaceView.flash(Color.RED, 50);
        }

        else if (!(info instanceof GoState)) return;

        else {
            state = (GoState)info;
            surfaceView.setState(state);
            CurrentTurn.setText("Turn: Player " + state.getTurn());
            P0captures.setText("Player 0 Captures: " + state.getPlayer0captures());
            P1captures.setText("Player 1 Captures: " + state.getPlayer1captures());
            surfaceView.invalidate();
            Logger.log(TAG, "receiving");
        }
    }

    @Override
    public void setAsGui(GameMainActivity activity) {
        // remember our activity
        myActivity = activity;

        // Load the layout resource for the new configuration
        activity.setContentView(R.layout.activity_main);

        // set the surfaceView instance variable
        surfaceView = (GoView)myActivity.findViewById(R.id.surfaceView);
        Logger.log("set listener", "OnTouch");
        surfaceView.setOnTouchListener(this);
        surfaceView.setState(state);

        //Set the values for the text
        CurrentTurn = (TextView)activity.findViewById(R.id.TurnView);
        P0captures = (TextView)activity.findViewById(R.id.player0captures);
        P1captures = (TextView)activity.findViewById(R.id.player1captures);
        //Values for the buttons
        ConfirmButton = (Button)activity.findViewById(R.id.button2);
        PassButton = (Button)activity.findViewById(R.id.button);


        ConfirmButton.setOnClickListener(this);
        PassButton.setOnClickListener(this);
    }
}
