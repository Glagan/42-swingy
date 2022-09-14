package org.glagan.Controller;

import org.glagan.View.Start;

public class StartController extends Controller {
    public StartController(org.glagan.Core.Swingy swingy) {
        super(swingy);
    }

    @Override
    public void reset() {

    }

    @Override
    public void run() {
        new Start().render();
        swingy.useSaveController();
    }

}
