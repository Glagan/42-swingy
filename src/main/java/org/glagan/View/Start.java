package org.glagan.View;

public class Start extends View {
    @Override
    public void gui() {
    }

    @Override
    public void console() {
        System.out.println("Welcome to Swingy !");
        System.out.println(
                "You are now in Console mode, you can switch to GUI mode at any moment by typing `set-display gui`.");
        System.out.println("A few global commands are available when you are prompted for a command:");
        System.out.println("set-display, [h]elp, home, quit");
        System.out.println("The other commands available to you are displayed above the prompt.");
        System.out.println("Enjoy the game !");
    }
}
