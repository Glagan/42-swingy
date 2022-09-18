package org.glagan.Core;

import org.glagan.Controller.Controller;
import org.glagan.Controller.FightController;
import org.glagan.Controller.GameController;
import org.glagan.Controller.SaveController;
import org.glagan.Controller.StartController;
import org.glagan.Display.CurrentDisplay;
import org.glagan.Display.Mode;
import org.glagan.Display.UI;

public class Swingy {
    static protected Swingy instance = new Swingy();

    protected Game game;
    protected boolean playing;
    protected UI ui;

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
        this.ui = new UI();
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public UI getUi() {
        return ui;
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

    public void consoleLoop() {
        while (CurrentDisplay.getMode().equals(Mode.CONSOLE) && this.playing && this.activeController != null) {
            this.activeController.run();
        }
    }

    static public Swingy getInstance() {
        return instance;
    }
}
