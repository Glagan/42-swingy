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
        System.out.println("The commands available to you are displayed above or before the input line.");
        System.out.println("Enjoy the game !");
    }
}
