package org.glagan.Controller;

import javax.swing.JFrame;

import org.glagan.Core.Swingy;
import org.glagan.Display.CurrentDisplay;
import org.glagan.Display.Mode;
import org.glagan.Display.UI;
import org.glagan.View.Help;

abstract public class Controller {
    protected Swingy swingy;

    protected Controller(Swingy swingy) {
        this.swingy = swingy;
    }

    /**
     * Handle global commands in console mode before handling controller commands
     *
     * @return true if the input contains a global command or else false
     */
    protected final boolean handleGlobalCommand(String input) {
        if (input == null) {
            return false;
        }
        if (input.equalsIgnoreCase("h") || input.equalsIgnoreCase("help")) {
            new Help(this).render();
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
                    CurrentDisplay.setMode(mode);
                    swingy.getUi().show();
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

    abstract public boolean handle(String event);

    abstract public void run();

    public final JFrame getFrame() {
        return swingy.getUi().getFrame();
    }

    public final UI getUi() {
        return swingy.getUi();
    }
}
