package org.glagan.View;

import org.glagan.Core.Game;
import org.glagan.Core.Input;

public class HeroSelectOrCreate extends View {
    public HeroSelectOrCreate(Game game) {
        super(game);
    }

    @Override
    public String getContext() {
        return "HeroSelectOrCreate";
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
