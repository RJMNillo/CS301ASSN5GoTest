package com.example.cs301assn5go.Go;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.example.cs301assn5go.R;
import com.example.cs301assn5go.game.GameFramework.GameFramework.utilities.FlashSurfaceView;
import com.example.cs301assn5go.game.GameFramework.GameFramework.utilities.Logger;

/**
 * The view of the game of Go
 *
 * @author Vandan Bhargava
 * @version April 2020
 */

public class GoView extends FlashSurfaceView {
    protected GoState state;
    protected Canvas canvas;
    private int widthForPieces;
    private int heightForPieces;
    public GoView(Context context) {
        super(context);
        init();
    }
    public GoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void onDraw(Canvas c){
        updateDimensions(c);
        Paint boardBackground = new Paint();
        boardBackground.setColor(Color.YELLOW);
        drawBoard(c);
        drawPieces(c);
        Paint name = new Paint();
        name.setStyle(Paint.Style.FILL);
        name.setColor(Color.BLACK);
        //draw name of player
    }

    public void updateDimensions(Canvas c){
        widthForPieces = c.getWidth();
        heightForPieces = c.getHeight();
    }

    private void init() { setBackgroundColor(Color.LTGRAY);}

    public void drawBoard(Canvas c){
        try{
            int[][] board = state.getBoard();
            int width = c.getWidth();
        int height = c.getHeight();
        float centerX = (float)width/2;
        float centerY = (float)height/2;
        Paint square = new Paint();
        square.setStyle(Paint.Style.STROKE);
        square.setColor(Color.BLACK);

        float boardSize = 0;
        if(width>height)
            boardSize = height - 50;
        else
            boardSize = width - 50;

        float x = centerX - (boardSize/2);
        float y = centerY - (boardSize/2);
        float squareSize = boardSize / 12;

            Paint boardBackground = new Paint();
            boardBackground.setColor(Color.rgb(255,212,35));
            boardBackground.setAlpha(155);
            c.drawRect(x,y,x+boardSize, y+boardSize, boardBackground);
        for(int i = 0; i<board.length-1; i++){
            for(int j = 0; j<board.length-1; j++){
                c.drawRect(x, y, x+squareSize, y+squareSize, square);
                x+=squareSize;
            }
            x = centerX - (boardSize/2);
            y+=squareSize;
        }
        } catch(NullPointerException e) {

        }

        //Code for drawing a board just a little bit in from the top left corner of screen
        //just in case the centered drawing above doesn't work
        /*
        float x = 25;
        float y = 25;
        float squareSize = 0;
        if(width > height)
            squareSize = (float) (height - 75) / board.length;
        else
            squareSize = (float) (width - 75) / board.length;

        for(int i = 0; i<board.length-1; i++){
            for(int j = 0; j<board.length-1; j++){
                c.drawRect(x, y, x+squareSize, y+squareSize, square);
                x+=squareSize;
            }
            x=25;
            y+=squareSize;
        }
        */
    }

    public void drawPieces(Canvas c){
        try {
            int[][] board = state.getBoard();
            canvas = c;
            Paint whiteStone = new Paint();
            whiteStone.setStyle(Paint.Style.FILL);
            whiteStone.setColor(Color.WHITE);
            Paint blackStone = new Paint();
            blackStone.setStyle(Paint.Style.FILL);
            blackStone.setColor(Color.BLACK);
            Paint whiteStone2 = new Paint();
            whiteStone2.setStyle(Paint.Style.FILL);
            int opacWhite = getResources().getColor(R.color.opacBlack);
            whiteStone2.setColor(opacWhite);
            Paint blackStone2 = new Paint();
            blackStone2.setStyle(Paint.Style.FILL);
            int opacBlack = getResources().getColor(R.color.opacWhite);
            blackStone2.setColor(opacBlack);

            float width = c.getWidth();
            float height = c.getHeight();
            float centerX = width / 2;
            float centerY = height / 2;

            float boardSize = 0;
            if (width > height)
                boardSize = height - 50;
            else
                boardSize = width - 50;
            float squareSize = boardSize / 12;

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] == 0) {
                        c.drawCircle((centerX - (boardSize / 2)) + (i * squareSize),
                                (centerY - (boardSize / 2)) + (j * squareSize), (squareSize / 20)*9, blackStone);
                    } else if (board[i][j] == 1) {
                        c.drawCircle((centerX - (boardSize / 2)) + (i * squareSize),
                                (centerY - (boardSize / 2)) + (j * squareSize), (squareSize / 20)*9, whiteStone);
                    } else if(board[i][j] == 4){
                        c.drawCircle((centerX - (boardSize / 2)) + (i * squareSize),
                                (centerY - (boardSize / 2)) + (j * squareSize), (squareSize / 20)*9, blackStone2);
                    } else if(board[i][j] == 5){
                        c.drawCircle((centerX - (boardSize / 2)) + (i * squareSize),
                                (centerY - (boardSize / 2)) + (j * squareSize), (squareSize / 20)*9, whiteStone2);
                    }
                }
            }
        } catch(NullPointerException e) {

        }
    }
    public Point mapPieces(float xPos, float yPos){
        //gets the lay of the board and the location
        int[][] board = state.getBoard();
        float width = widthForPieces;
        float height = heightForPieces;
        float centerX = width/2;
        float centerY = height/2;
        float boardSize = 0;
        if(width>height)
            boardSize = height - 50;
        else
            boardSize = width - 50;
        Logger.log("View", "boardsize: " + boardSize);
        float squareSize = boardSize / 12;
        Logger.log("View", "squareSize: " + squareSize);

        //creates a virtual rectangle 2D array that servers as the hitbox of each intersection
        Rect[][] rectTouch = new Rect[board.length][board[0].length];
        for(int i = 0; i<rectTouch.length; i++){
            for(int j = 0; j<rectTouch.length; j++){
                int left = (int)(centerX - (boardSize/2) - (squareSize/2) + 1);
                int top = (int)(centerY - (boardSize/2) - (squareSize/2) + 1);
                int right = (int)(centerX - (boardSize/2) + (squareSize/2) - 1);
                int bottom = (int)(centerY - (boardSize/2) + (squareSize/2) - 1);
                rectTouch[i][j] = new Rect((int)(left+(i*squareSize)), (int)(top+(j*squareSize)),
                        (int)(right+(i*squareSize)), (int)(bottom+(j*squareSize)));
            }
        }

        Logger.log("View", "xPos: " + xPos + " yPos: " + yPos);
        Point point = new Point(board.length, board.length);

        //checks to see if the xPos and yPos are within that rectangle from the 2D array
        //if it is then returns the x and y values of the array in a point
        //if not returns an empty point
        for(int i = 0; i<rectTouch.length; i++){
            for(int j = 0; j<rectTouch.length; j++){
                Logger.log("View", "xPos : " + xPos + " Rect left and right: " + rectTouch[i][j].left + " " +
                        rectTouch[i][j].right);
                Logger.log("View", "yPos: " + yPos + " Rect top and bottom: " + rectTouch[i][j].top + " " +
                        rectTouch[i][j].bottom);
                if(xPos > rectTouch[i][j].left && xPos < rectTouch[i][j].right &&
                        yPos > rectTouch[i][j].top && yPos < rectTouch[i][j].bottom){
                    point.set(i, j);
                    return point;
                }
            }
        }
        return point;
    }

    public void setState(GoState state) { this.state = state; }
}
