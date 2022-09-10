package org.glagan.Core;

import java.util.HashMap;

import org.glagan.Controller.Controller;
import org.glagan.Controller.GameController;
import org.glagan.Controller.MapController;
import org.glagan.Controller.StartController;
import org.glagan.Display.Display;
import org.glagan.Display.DisplayFactory;
import org.glagan.Display.Mode;
import org.glagan.View.HeroSelection;
import org.glagan.View.Start;
import org.glagan.View.View;

import jakarta.validation.constraints.NotNull;

public class Game {
    @NotNull
    protected Display display;

    protected boolean playing;

    protected HashMap<String, Controller> controllers;

    public Game(Display display) {
        this.display = display;
        this.display.setView(new Start(this));
        playing = true;
        controllers = new HashMap<>();
        controllers.put("Start", new StartController(this));
        controllers.put("Map", new GameController(this));
        controllers.put("Game", new MapController(this));
    }

    public void run() {
        while (playing) {
            display.run();
        }
    }

    public void setView(View view) {
        this.display.setView(view);
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
                display.setView(new HeroSelection(this));
                error = "";
                break;
        }

        // * Map Action to Controller and execute logic
        if (error == null) {
            Controller controller = controllers.get(action.getContext());
            if (controller != null) {
                error = controller.execute(action);
            } else {
                error = "Unknown context `" + action.getContext() + "`";
            }
        }

        // * Display error
        if (error != null && !error.equals("")) {
            System.out.println("error: " + error);
        }
    }
}
