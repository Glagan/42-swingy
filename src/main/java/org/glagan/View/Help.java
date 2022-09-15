package org.glagan.View;

public class Help extends View {
    @Override
    public void gui() {
        // TODO Auto-generated method stub

    }

    @Override
    public void console() {
        System.out.println("\n----");
        System.out.println("Help:");
        System.out.println("Global commands");
        System.out.println("`help`: display this help");
        System.out.println("`set-display {console,gui}`: change the game display            mode");
        System.out.println("`quit`: quit the game");
        System.out.println("`home`: go to the home screen");
        System.out.println("----");
    }
}
