package com.example.cs301assn5go.Go;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;

import com.example.cs301assn5go.game.GameFramework.GameFramework.utilities.FlashSurfaceView;

/**
 *
 */

public class GoView extends FlashSurfaceView {
    protected GoState state;
    public GoView(Context context) {
        super(context);
    }

    public void onDraw(Canvas c){
        //drawBoard(c);
        //drawPieces(c, state);
    }
    public void drawBoard(Canvas c){

    }
    public void drawPieces(Canvas C, GoState s){
        int[][] board = s.getBoard();

        //draw through mapPieces(x, y)
    }

    public Point mapPieces(int xPos, int yPos){
        //translates board pos to draw coordinate
        return new Point();
    }
}
