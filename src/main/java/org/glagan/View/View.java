package org.glagan.View;

import org.glagan.Core.Action;
import org.glagan.Core.ActionBuilder;
import org.glagan.Core.Game;

abstract public class View {
    protected Game game;

    public View(Game game) {
        this.game = game;
    }

    abstract public String getContext();

    abstract public void displayOnConsole();

    abstract public void displayInGUI();

    protected void dispatch(String action, String[] arguments) {
        this.game.dispatch(new Action(action, this.getContext(), arguments));
    }

    protected ActionBuilder action() {
        return new ActionBuilder().context(this.getContext());
    }
}
