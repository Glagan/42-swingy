package org.glagan.Controller;

import org.glagan.Core.Swingy;
import org.glagan.Display.Display;
import org.glagan.Display.Mode;

abstract public class Controller {
    protected Swingy swingy;

    protected Controller(Swingy swingy) {
        this.swingy = swingy;
    }

    protected boolean handleGlobalCommand(String input) {
        if (input.equalsIgnoreCase("h") || input.equalsIgnoreCase("help")) {
            System.out.println("\n----");
            System.out.println("Help:");
            System.out.println("Global commands");
            System.out.println("`help`: display this help");
            System.out.println("`set-display {console,gui}`: change the game display            mode");
            System.out.println("`quit`: quit the game");
            System.out.println("`home`: go to the home screen");
            System.out.println("----");
            return true;
        } else if (input.equalsIgnoreCase("home")) {
            swingy.useSaveController();
            return true;
        } else if (input.equalsIgnoreCase("quit")) {
            swingy.quit();
            return true;
        } else if (input.startsWith("set-display ")) {
            String[] parts = input.split(" ");
            if (parts.length == 2) {
                Mode mode = Mode.fromString(parts[1]);
                if (mode != null) {
                    Display.setDisplay(mode);
                } else {
                    System.out.println("Invalid set-display `" + parts[1] + "`, expected `console` or `gui`");
                }
            } else {
                System.out.println("Invalid set-display arguments, expected 1 argument");
            }
            return true;
        }
        return false;
    }

    abstract public void reset();

    abstract public void run();
}
