package org.glagan;

import java.util.logging.Level;

import org.glagan.Core.Save;
import org.glagan.Core.Swingy;
import org.glagan.Display.CurrentDisplay;
import org.glagan.Display.Mode;

public class App {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        Mode displayMode = Mode.CONSOLE;
        if (args.length == 1) {
            switch (args[0]) {
                case "console":
                    displayMode = Mode.CONSOLE;
                    break;
                case "gui":
                    displayMode = Mode.GUI;
                    break;
            }
        }
        CurrentDisplay.setMode(displayMode);

        // Ensure the saves directory exists
        if (!Save.ensureSavesDirectoryExists()) {
            return;
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (CurrentDisplay.getMode().equals(Mode.GUI)) {
                    Swingy.getInstance().getUi().show();
                } else {
                    Swingy.getInstance().consoleLoop();
                }
            }
        });
    }
}
