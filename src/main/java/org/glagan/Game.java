package org.glagan;

import org.glagan.Display.Display;
import org.glagan.View.StartView;

import jakarta.validation.constraints.NotNull;

public class Game {
    @NotNull
    protected Display display;

    public Game(Display display) {
        this.display = display;
        this.display.setView(new StartView());
    }

    public void run() {
        this.display.run();
    }
}
