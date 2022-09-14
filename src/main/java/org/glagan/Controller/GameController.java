package org.glagan.Controller;

import org.glagan.Adapters.GsonCustomBuilder;
import org.glagan.Core.Game;

enum GameState {
    INIT,
    MAP,
}

public class GameController extends Controller {
    protected GameState state;

    public GameController(org.glagan.Core.Swingy swingy) {
        super(swingy);
        this.reset();
    }

    @Override
    public void reset() {
        this.state = GameState.INIT;

    }

    @Override
    public void run() {
        // Check in which state we currently are and generate game accordingly
        Game game = swingy.getGame();
        if (game.getMap() == null) {
            game.generateNewMap();
            game.save();
            System.out.println(GsonCustomBuilder.getBuilder().create().toJson(game.getMap()));
        }
    }

}
