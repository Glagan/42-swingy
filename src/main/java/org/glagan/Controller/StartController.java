package org.glagan.Controller;

import org.glagan.View.Start;

public class StartController extends Controller {
    public StartController(org.glagan.Core.Swingy swingy) {
        super(swingy);
    }

    public void reset() {
    }

    public boolean handle(String event) {
        if (event.equals("continue")) {
            swingy.useSaveController();
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        new Start(this).render();
    }

}
