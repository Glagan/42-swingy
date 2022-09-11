package org.glagan.View;

import org.glagan.Core.Game;
import org.glagan.Core.Input;

public class HeroSelection extends View {
    public HeroSelection(Game game) {
        super(game);
    }

    @Override
    public String getContext() {
        return "HeroSelection";
    }

    @Override
    public void displayInGUI() {
    }

    @Override
    public void displayOnConsole() {
        System.out.println("Select your hero");
        // TODO Display all heroes
        String input = Input.ask("> [s]elect {number} [c]reate", null);
        this.action().from(input).dispatch(game);
    }
}