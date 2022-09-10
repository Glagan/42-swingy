package org.glagan.Core;

import org.glagan.Display.Display;
import org.glagan.Display.DisplayFactory;
import org.glagan.Display.Mode;
import org.glagan.View.HeroSelectOrCreate;
import org.glagan.View.Start;
import org.glagan.View.View;

import jakarta.validation.constraints.NotNull;

public class Game {
    @NotNull
    protected Display display;

    protected boolean playing;

    public Game(Display display) {
        this.display = display;
        this.display.setView(new Start(this));
        playing = true;
    }

    public void run() {
        while (playing) {
            display.run();
        }
    }

    public void dispatch(Action action) {
        // * Global commands
        String error = null;
        switch (action.getAction()) {
            case "set-display":
                if (action.hasAtLeastXArguments(1)) {
                    String stringMode = action.getArgument(0);
                    Mode mode = Mode.fromString(stringMode);
                    if (mode != null) {
                        View currentView = display.getView();
                        display = DisplayFactory.newDisplay(mode);
                        display.setView(currentView);
                    } else {
                        error = "Invalid display mode (`console`, `gui`)";
                    }
                } else {
                    error = "`set-display` need an argument, the display mode (`console`, `gui`)";
                }
                break;
            case "help":
                System.out.println("----");
                System.out.println("Help:");
                System.out.println("Global commands");
                System.out.println("`help`: display this help");
                System.out.println("`set-display {console,gui}`: change the game display mode");
                System.out.println("`quit`: quit the game");
                System.out.println("`home`: go to the home screen");
                System.out.println("----");
                error = "";
                break;
            case "quit":
                playing = false;
                error = "";
                break;
            case "home":
                display.setView(new HeroSelectOrCreate(this));
                error = "";
                break;
        }

        // * Map Action to Controller and execute logic
        if (error == null) {
            if (action.getContext().equals("Start") && action.getAction().equals("continue")) {
                display.setView(new HeroSelectOrCreate(this));
            }
        }

        // * Display error
        if (error != null && !error.equals("")) {
            System.out.println("error: " + error);
        }
    }
}
