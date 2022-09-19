package org.glagan.View;

import org.glagan.Controller.Controller;

public class RunSuccess extends View {
    public RunSuccess(Controller controller) {
        super(controller);
    }

    @Override
    public void gui() {
    }

    @Override
    public void console() {
        System.out.println("You managed to run away from your opponent !");
    }
}
