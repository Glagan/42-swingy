package org.glagan.View;

import org.glagan.Controller.Controller;

public class RunFailure extends View {
    public RunFailure(Controller controller) {
        super(controller);
    }

    @Override
    public void gui() {
    }

    @Override
    public void console() {
        System.out.println("You failed to run away from your opponent !");
    }
}
