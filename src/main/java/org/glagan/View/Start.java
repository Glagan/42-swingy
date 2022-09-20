package org.glagan.View;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import org.glagan.Controller.Controller;

import net.miginfocom.swing.MigLayout;

public class Start extends View implements ActionListener {
    static protected String[] guiIntroduction = {
            "You are now in GUI mode, you can switch to Console mode by",
            "going in the View menu and selecting the Console item.",
            "You can also quit the game or go back to the hero selection",
            "trough the menu.",
            "Enjoy the game !"
    };

    static protected String[] consoleIntroduction = {
            "You are now in Console mode, you can switch to GUI mode at any moment by typing `set-display gui`.",
            "A few global commands are available when you are prompted for a command:",
            "set-display {gui}, [h]elp, home, quit",
            "The other commands available to you are displayed above the prompt.",
            "Enjoy the game !"
    };

    public Start(Controller controller) {
        super(controller);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("continue")) {
            dispatch("continue");
        }
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new MigLayout("fillx"));

        panel.add(new JLabel("Welcome to Swingy !"), "span, wrap, center");

        for (String line : guiIntroduction) {
            panel.add(new JLabel(line), "span, wrap");
        }

        JButton play = new JButton("Play");
        play.setMnemonic(KeyEvent.VK_P);
        play.setActionCommand("continue");
        play.addActionListener(this);
        play.setHorizontalAlignment(JButton.CENTER);
        play.requestFocusInWindow();
        panel.add(play, "span, center");

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void console() {
        System.out.println("Welcome to Swingy !");
        for (String line : consoleIntroduction) {
            System.out.println(line);
        }
        dispatch("continue");
    }
}
