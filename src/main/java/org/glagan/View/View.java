package org.glagan.View;

import org.glagan.Core.Action;
import org.glagan.Core.ActionBuilder;
import org.glagan.Core.Swingy;

abstract public class View {
    protected Swingy swingy;

    public View(Swingy swingy) {
        this.swingy = swingy;
    }

    abstract public String getContext();

    abstract public void displayOnConsole();

    abstract public void displayInGUI();

    protected void dispatch(String action, String[] arguments) {
        this.swingy.dispatch(new Action(action, this.getContext(), arguments));
    }

    protected ActionBuilder action() {
        return new ActionBuilder().context(this.getContext());
    }
}
