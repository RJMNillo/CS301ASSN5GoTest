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
 * @author Vandan Bhargava (coding), Reggie Jan Marc Nillo (documentation)
 * @version April 2020
 */

public class GoView extends FlashSurfaceView
{
    //Instance Variables
    /** The state of the game of Go */
    protected GoState state;

    /** Canvas of which board and pieces will be drawn on */
    protected Canvas canvas;

    /** Width of Piece */
    private int widthForPieces;

    /** Height of Piece */
    private int heightForPieces;

    //methods

    /**
     * constructor for GoView, Calls upon super constructor
     * @param context The context in which this view will be set up with
     */
    public GoView(Context context) {
        super(context);
        init();
    }

    /**
     * constructor for GoView, Calls upon super constructor
     * @param context The context in which this view will be set up with
     * @param attrs This set of attributes
     */
    public GoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Draws the board and its pieces, visual state of the game
     * @param c The canvas in which the program will draw on
     */
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

    /**
     * Sets the Dimensions of the board
     * @param c Canvas to draw
     */
    public void updateDimensions(Canvas c){
        widthForPieces = c.getWidth();
        heightForPieces = c.getHeight();
    }

    /**
     * Gives a background color for the game view
     */
    private void init() { setBackgroundColor(Color.LTGRAY);}

    /**
     * Draws the board with the intersections
     * @param c Canvas to draw on
     */
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
    }

    /**
     * Draws the pieces as stated from the board
     * @param c Canvas to draw on
     */
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

    /**
     * Translate an onTouch point into a point of the closest intersection.
     * @param xPos x coordinate from touch
     * @param yPos y coordinate from touch
     * @return A point on an intersection of the board
     */
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

        float squareSize = boardSize / 12;

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

        Point point = new Point(board.length, board.length);

        //checks to see if the xPos and yPos are within that rectangle from the 2D array
        //if it is then returns the x and y values of the array in a point
        //if not returns an empty point not on the board
        for(int i = 0; i<rectTouch.length; i++){
            for(int j = 0; j<rectTouch.length; j++){
                if(xPos > rectTouch[i][j].left && xPos < rectTouch[i][j].right &&
                        yPos > rectTouch[i][j].top && yPos < rectTouch[i][j].bottom){
                    point.set(i, j);
                    return point;
                }
            }
        }
        return point;
    }

    /**
     * Set state to the current state
     * @param state State of the game
     */
    public void setState(GoState state) { this.state = state; }
}
