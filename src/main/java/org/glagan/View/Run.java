package org.glagan.View;

import org.glagan.Controller.Controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class Run extends View implements ActionListener {
    protected boolean success;

    public Run(Controller controller, boolean success) {
        super(controller);
        this.success = success;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("fight")) {
            dispatch("fight");
        } else if (action.equals("continue")) {
            dispatch("continue");
        }
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new MigLayout("fillx"));

        JPanel actionPanel = new JPanel(new MigLayout("fillx, align center, insets 0"));
        if (success) {
            panel.add(new JLabel("You managed to run away from your opponent !"), "span, wrap, center");
            JButton backButton = new JButton("Back");
            backButton.setActionCommand("continue");
            backButton.addActionListener(this);
            backButton.requestFocusInWindow();
            actionPanel.add(backButton, "span, center");
        } else {
            panel.add(new JLabel("You failed to run away from your opponent !"), "span, wrap, center");
            panel.add(new JLabel("Prepare to fight !"), "span, wrap, center");
            JButton fightButton = new JButton("Fight");
            fightButton.setActionCommand("fight");
            fightButton.addActionListener(this);
            fightButton.requestFocusInWindow();
            actionPanel.add(fightButton, "span, center");
        }
        panel.add(actionPanel, "span, grow, center");

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void console() {
        if (success) {
            System.out.println("You managed to run away from your opponent !");
            waitGoBack("Press enter to go back");
        } else {
            System.out.println("You failed to run away from your opponent !");
            dispatch("fight");
        }
    }
}
