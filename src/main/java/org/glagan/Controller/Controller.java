package org.glagan.Controller;

import org.glagan.Core.Swingy;

abstract public class Controller {
    protected Swingy swingy;

    protected Controller(Swingy swingy) {
        this.swingy = swingy;
    }

    abstract public void reset();

    abstract public void run();
}
