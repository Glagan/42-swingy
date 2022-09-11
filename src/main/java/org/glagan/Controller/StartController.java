package org.glagan.Controller;

import java.util.HashMap;

import org.glagan.Core.Action;
import org.glagan.View.HeroSelection;

public class StartController extends Controller {
    public StartController(org.glagan.Core.Swingy swingy) {
        super(swingy, "Start", new HashMap<String, String>() {
            {
                put("continue", "menuContinue");
            }
        });
    }

    public String menuContinue(Action action) {
        swingy.setView(new HeroSelection(swingy));
        return null;
    }
}
