package com.example.cs301assn5go.Go;

import android.view.MotionEvent;
import android.view.View;

import com.example.cs301assn5go.game.GameFramework.GameFramework.GameHumanPlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.GameMainActivity;
import com.example.cs301assn5go.game.GameFramework.GameFramework.infoMessage.GameInfo;

public class GoHumanPlayer extends GameHumanPlayer implements View.OnTouchListener, View.OnClickListener
{

    private GameMainActivity someactivity;
    /**
     * constructor
     *
     * @param name the name of the player
     */
    public GoHumanPlayer(String name) {
        super(name);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

    }

    @Override
    public void setAsGui(GameMainActivity activity)
    {
        //Get the activity
    }
}
