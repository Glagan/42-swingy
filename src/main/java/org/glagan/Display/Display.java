package org.glagan.Display;

import org.glagan.Core.Game;
import org.glagan.View.View;

public abstract class Display {
    protected Game game;

    protected View view;

    protected Display() {
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    abstract public void run();
}
