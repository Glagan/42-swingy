package org.glagan.View;

import org.glagan.Display.Display;
import org.glagan.Display.Mode;

abstract public class View {
    abstract public void console();

    abstract public void gui();

    final public void render() {
        Mode mode = Display.getDisplay();
        if (mode.equals(Mode.CONSOLE)) {
            this.console();
        } else {
            this.gui();
        }
    }
}
