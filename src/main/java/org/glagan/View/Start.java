package org.glagan.View;

import org.glagan.Core.Game;

public class Start extends View {
    public Start(Game game) {
        super(game);
    }

    @Override
    public String getContext() {
        return "Start";
    }

    @Override
    public void displayInGUI() {
    }

    @Override
    public void displayOnConsole() {
        System.out.println("Welcome to Swingy !");
        System.out.println(
                "You are now in Console mode, you can switch to GUI mode at any moment by typing `set-display gui`.");
        System.out.println("The commands available to you are displayed above or before the input line.");
        System.out.println("Enjoy the game !");
        this.dispatch("continue", null);
    }
}
