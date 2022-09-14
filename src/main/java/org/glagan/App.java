package org.glagan;

// import org.glagan.Character.Warrior;
// import org.glagan.Core.Game;
import org.glagan.Core.Save;
import org.glagan.Core.Swingy;
import org.glagan.Display.Display;
import org.glagan.Display.Mode;
// import org.glagan.World.Map;

public class App {
    public static void main(String[] args) {
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
        Display.setDisplay(displayMode);

        // Game test = new Game(new Warrior("Glagan"), new Map("Test", 1, 5, null),
        // null, null);
        // System.out.println(test.serialize());

        // Ensure the saves directory exists
        if (!Save.ensureSavesDirectoryExists()) {
            return;
        }

        Swingy swingy = new Swingy();
        swingy.run();
    }
}
