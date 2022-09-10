package org.glagan.Display;

public class DisplayFactory {
    public static Display newDisplay(Mode mode) {
        if (mode.equals(Mode.CONSOLE)) {
            return new Console();
        }
        return new GUI();
    }
}
