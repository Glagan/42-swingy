package org.glagan.Display;

public class GUI extends Display {
    @Override
    public void run() {
        if (this.view != null) {
            this.view.displayInGUI();
        }
    }
}
