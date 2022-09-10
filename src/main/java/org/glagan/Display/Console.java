package org.glagan.Display;

public class Console extends Display {
    @Override
    public void run() {
        if (this.view != null) {
            this.view.displayOnConsole();
        }
    }
}
