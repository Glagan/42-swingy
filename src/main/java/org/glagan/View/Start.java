package org.glagan.View;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("continue")) {
            dispatch("continue");
        }

    }

    @Override
    public void gui() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JLabel welcome = new JLabel("Welcome to Swingy !");
        welcome.setHorizontalAlignment(JLabel.CENTER);
        panel.add(welcome);

        JTextArea text = new JTextArea();
        text.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setOpaque(false);
        for (String line : guiIntroduction) {
            text.append(line + "\n");
        }
        panel.add(text);

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
        play.requestFocusInWindow();
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
