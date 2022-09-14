package org.glagan.Display;

public abstract class Display {
    static protected Mode mode;

    protected Display() {
    }

    static public void setDisplay(Mode mode) {
        Display.mode = mode;
    }

    static public Mode getDisplay() {
        return Display.mode;
    }
}
