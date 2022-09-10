package org.glagan.Controller;

import java.util.HashMap;

public class GameController extends Controller {
    public GameController(org.glagan.Core.Game game) {
        super(game, "Game", new HashMap<String, String>() {
            {
            }
        });
    }
}
