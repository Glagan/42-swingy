package org.glagan.Display;

public enum Mode {
    CONSOLE,
    GUI;

    public static Mode fromString(String text) {
        if (text != null) {
            for (Mode b : Mode.values()) {
                if (b.toString().equalsIgnoreCase(text)) {
                    return b;
                }
            }
        }
        return null;
    }
}
