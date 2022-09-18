package org.glagan.Display;

public abstract class CurrentDisplay {
    static protected Mode mode;

    protected CurrentDisplay() {
    }

    static public void setMode(Mode mode) {
        if (mode != CurrentDisplay.mode) {
            CurrentDisplay.mode = mode;
        }
    }

    static public Mode getMode() {
        return CurrentDisplay.mode;
    }
}
