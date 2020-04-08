package com.example.cs301assn5go.Go;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        drawBoard(c);
        drawPieces(c);
    }
    public void drawBoard(Canvas c){
        int[][] board = state.getBoard();
        int width = c.getWidth();
        int height = c.getHeight();
        float centerX = (float)width/2;
        float centerY = (float)height/2;

        //for now drawing the board a bit in from the top left not necessarily centered
        Paint square = new Paint();
        square.setStyle(Paint.Style.STROKE);
        square.setColor(Color.BLACK);
        float x = 25;
        float y = 25;
        float squareSize = 0;
        if(width > height)
            squareSize = (float) (height - 75) / board.length;
        else
            squareSize = (float) (width - 75) / board.length;

        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board.length; j++){
                c.drawRect(x, y, x+squareSize, y+squareSize, square);
                x+=squareSize;
            }
            x=25;
            y+=squareSize;
        }

    }
    public void drawPieces(Canvas c){
        int[][] board = state.getBoard();

        Paint whiteStone = new Paint();
        whiteStone.setStyle(Paint.Style.FILL);
        whiteStone.setColor(Color.WHITE);
        Paint blackStone = new Paint();
        blackStone.setStyle(Paint.Style.FILL);
        blackStone.setColor(Color.WHITE);

        float width = c.getWidth();
        float height = c.getHeight();
        float squareSize = 0;
        if(width > height)
            squareSize = (height - 75) / board.length;
        else
            squareSize = (width - 75) / board.length;

        for(int i = 0; i<board.length; i++){
            for(int j = 0; j<board.length; j++){
                if(board[i][j] == 0){
                    c.drawCircle(25+(i*squareSize),25+(j*squareSize), (squareSize/3), blackStone);
                } else if(board[i][j] == 1){
                    c.drawCircle(25+(i*squareSize), 25+(j*squareSize), (squareSize/3), whiteStone);
                }
            }
        }
    }
    public Point mapPieces(int xPos, int yPos){

        return new Point();
    }
}
