package org.glagan.View;

import org.glagan.Controller.Controller;

public class Help extends View {
    public Help(Controller controller) {
        super(controller);
    }

    @Override
    public void gui() {
    }

    @Override
    public void console() {
        System.out.println("\n----");
        System.out.println("Help:");
        System.out.println("You need to reach the border of the map to clear it, a new one will be generated.");
        System.out.println("Dots on the map represent enemies, you will have to fight them if you walk near them.");
        System.out.println("You can try to run away from an enemy but it only works 50% of the times.");
        System.out.println("Global commands");
        System.out.println("`help`: display this help");
        System.out.println("`set-display {console,gui}`: change the game display mode");
        System.out.println("`quit`: quit the game");
        System.out.println("`home`: go to the home screen");
        System.out.println("----");
    }
}
