package org.glagan.Display;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class UI {
    protected JFrame frame;

    public UI() {
        frame = new JFrame("Swingy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Loading...");
        frame.getContentPane().add(label);
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    public void repaint() {
        frame.pack();
        frame.repaint();
    }

    public void hide() {
        frame.setVisible(false);
    }

    public JFrame getFrame() {
        return frame;
    }
}
