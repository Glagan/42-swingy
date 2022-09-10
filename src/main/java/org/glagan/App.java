package org.glagan;

import org.glagan.Display.DisplayFactory;
import org.glagan.Display.Mode;

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

        Game game = new Game(DisplayFactory.newDisplay(displayMode));
        System.out.println("Game " + game);
        game.run();
    }
}
