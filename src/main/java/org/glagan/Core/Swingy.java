package org.glagan.Core;

import org.glagan.Controller.Controller;
import org.glagan.Controller.FightController;
import org.glagan.Controller.GameController;
import org.glagan.Controller.SaveController;
import org.glagan.Controller.StartController;

public class Swingy {
    protected Game game;
    protected boolean playing;

    protected StartController startController;
    protected SaveController saveController;
    protected GameController gameController;
    protected FightController fightController;

    protected Controller activeController;

    public Swingy() {
        this.playing = true;
        this.startController = new StartController(this);
        this.saveController = new SaveController(this);
        this.gameController = new GameController(this);
        this.fightController = new FightController(this);
        this.activeController = this.startController;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    protected void useController(Controller controller) {
        this.activeController = controller;
    }

    public void useStartController() {
        this.activeController.reset();
        this.useController(this.startController);
    }

    public void useSaveController() {
        this.activeController.reset();
        this.useController(this.saveController);
    }

    public void useGameController() {
        this.activeController.reset();
        this.useController(this.gameController);
    }

    public void useFightController() {
        this.activeController.reset();
        this.useController(this.fightController);
    }

    public void quit() {
        this.playing = false;
    }

    // public void dispatch(Action action) {
    // // * Global commands
    // String error = null;
    // switch (action.getAction()) {
    // case "set-display":
    // if (action.hasAtLeastXArguments(1)) {
    // String stringMode = action.getArgument(0);
    // Mode mode = Mode.fromString(stringMode);
    // if (mode != null) {
    // display = DisplayFactory.newDisplay(mode);
    // this.reload();
    // } else {
    // error = "Invalid display mode (`console`, `gui`)";
    // }
    // } else {
    // error = "`set-display` need an argument, the display mode (`console`,
    // `gui`)";
    // }
    // break;
    // case "help":
    // System.out.println("----");
    // System.out.println("Help:");
    // System.out.println("Global commands");
    // System.out.println("`help`: display this help");
    // System.out.println("`set-display {console,gui}`: change the game display
    // mode");
    // System.out.println("`quit`: quit the game");
    // System.out.println("`home`: go to the home screen");
    // System.out.println("----");
    // error = "";
    // break;
    // case "quit":
    // playing = false;
    // error = "";
    // break;
    // case "home":
    // this.display.setView(new HeroSelection(this));
    // error = "";
    // break;
    // }

    // // * Map Action to Controller and execute logic
    // if (error == null) {
    // Controller controller = controllers.get(action.getContext());
    // if (controller != null) {
    // error = controller.execute(action);
    // } else {
    // error = "Unknown context `" + action.getContext() + "`";
    // }
    // }

    // // * Display error
    // if (error != null && !error.equals("")) {
    // System.out.println("error: " + error);
    // }
    // }

    public void run() {
        while (this.playing && this.activeController != null) {
            this.activeController.run();
        }
    }
}
