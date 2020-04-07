package com.example.cs301assn5go.Go;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cs301assn5go.R;
import com.example.cs301assn5go.game.GameFramework.GameFramework.Game;
import com.example.cs301assn5go.game.GameFramework.GameFramework.GameMainActivity;
import com.example.cs301assn5go.game.GameFramework.GameFramework.GamePlayer;
import com.example.cs301assn5go.game.GameFramework.GameFramework.LocalGame;
import com.example.cs301assn5go.game.GameFramework.GameFramework.gameConfiguration.GameConfig;
import com.example.cs301assn5go.game.GameFramework.GameFramework.gameConfiguration.GamePlayerType;

import java.util.ArrayList;

/**
 * this is the primary activity for Go game
 *
 * @author Braeden Lane
 * @version April 2020
 */
public class GoActivity extends GameMainActivity {
    //Tag for logging
    private static final String TAG = "GoActivity";
    public static final int PORT_NUMBER = 5213;
    /**
     * a Go game is for two players. The default is human vs. computer
     */
    @Override
    public GameConfig createDefaultConfig() {

        // Define the allowed player types
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        playerTypes.add(new GamePlayerType("Local Human Player") {
            public GamePlayer createPlayer(String name) {
                return new GoHumanPlayer(name);
            }
        });

        playerTypes.add(new GamePlayerType("Computer Player (Dumb)") {
            public GamePlayer createPlayer(String name) {
                return new GoDumbAI(name);
            }
        });


        playerTypes.add(new GamePlayerType("Computer Player (Smart)") {
            public GamePlayer createPlayer(String name) {
                return new GoSmartAI(name);
            }
        });

        // Create a game configuration class for Go
        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 2, "Go", PORT_NUMBER);

        // Add the default players
        defaultConfig.addPlayer("Human", 0);
        defaultConfig.addPlayer("Computer", 1);

        // done!
        return defaultConfig;
    }

    /**
     * createLocalGame
     *
     * Creates a new game that runs on the server tablet,
     *
     * @return a new, game-specific instance of a sub-class of the LocalGame
     *         class.
     */
    @Override
    public LocalGame createLocalGame() { return new GoLocalGame(); }

}
