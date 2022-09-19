package org.glagan.View;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import org.glagan.Controller.Controller;

public class Start extends View implements ActionListener {
    static protected String[] guiIntroduction = {
            "You are now in GUI mode, you can switch to Console mode at any moment by clicking on the Console button.",
            "If you need help you can press the help button at any moment.",
            "Enjoy the game !"
    };

    static protected String[] consoleIntroduction = {
            "You are now in Console mode, you can switch to GUI mode at any moment by typing `set-display gui`.",
            "A few global commands are available when you are prompted for a command:",
            "set-display, [h]elp, home, quit",
            "The other commands available to you are displayed above the prompt.",
            "Enjoy the game !"
    };

    public Start(Controller controller) {
        super(controller);
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new GridLayout(5, 1));

        JLabel welcome = new JLabel("Welcome to Swingy !");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        panel.add(welcome);

        for (String line : guiIntroduction) {
            JLabel label = new JLabel(line);
            panel.add(label);
        }

        JButton play = new JButton("Play");
        play.setMnemonic(KeyEvent.VK_P);
        play.setActionCommand("continue");
        play.addActionListener(this);
        play.setHorizontalAlignment(JButton.CENTER);
        JPanel playPanel = new JPanel();
        playPanel.add(play);
        panel.add(playPanel);

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println(event.getActionCommand());
        System.out.println(event);
        if ("continue".equals(event.getActionCommand())) {
            dispatch("continue");
        }

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
