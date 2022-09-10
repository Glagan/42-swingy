package org.glagan.Controller;

import java.util.HashMap;

import org.glagan.Core.Action;
import org.glagan.Core.Game;
import org.glagan.View.HeroSelection;

public class StartController extends Controller {
    public StartController(Game game) {
        super(game, "Start", new HashMap<String, String>() {
            {
                put("continue", "menuContinue");
            }
        });
    }

    public String menuContinue(Action action) {
        game.setView(new HeroSelection(game));
        return null;
    }
}
