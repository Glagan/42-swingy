package org.glagan;

// import org.glagan.Character.Warrior;
// import org.glagan.Core.Game;
import org.glagan.Core.Save;
import org.glagan.Core.Swingy;
import org.glagan.Display.DisplayFactory;
import org.glagan.Display.Mode;
// import org.glagan.Map.Map;

/**
 * Hello world!
 *
 */
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

        // Ensure the saves directory exists
        if (!Save.ensureSavesDirectoryExists()) {
            return;
        }

        // Game test = new Game(new Warrior("Glagan"), new Map("Test", 1, 5, null),
        // null, null);
        // System.out.println(test.serialize());

        // String[] files = Save.listSaveFiles();
        // System.out.println("Found " + files.length + " saves");
        // for (String string : files) {
        // System.out.println("save: " + string);
        // }

        Swingy swingy = new Swingy(DisplayFactory.newDisplay(displayMode));
        System.out.println("Swingy " + swingy);
        swingy.run();
    }
}
