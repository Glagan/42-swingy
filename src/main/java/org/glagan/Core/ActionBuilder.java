package org.glagan.Core;

import java.util.Arrays;

public class ActionBuilder {
    protected String _action;

    protected String _context;

    protected String[] _arguments;

    public ActionBuilder from(String line) {
        String[] lineSplit = line.split(" ");
        if (lineSplit.length == 0) {
            return this;
        }
        return this.action(lineSplit[0]).arguments(Arrays.copyOfRange(lineSplit, 1, lineSplit.length));
    }

    public ActionBuilder action(String action) {
        this._action = action;
        return this;
    }

    public ActionBuilder context(String context) {
        this._context = context;
        return this;
    }

    public ActionBuilder arguments(String[] arguments) {
        this._arguments = arguments;
        return this;
    }

    public Action build() {
        return new Action(this._action, this._context, this._arguments);
    }

    public void dispatch(Game game) {
        Action action = this.build();
        game.dispatch(action);
    }
}
