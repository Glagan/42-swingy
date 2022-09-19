package org.glagan.Display;

import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.plaf.DimensionUIResource;

import org.glagan.Core.Swingy;

public class UI implements ActionListener {
    protected JFrame frame;

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equalsIgnoreCase("change-display")) {
            CurrentDisplay.setMode(Mode.CONSOLE);
            hide();
            Swingy.getInstance().consoleLoop();
        } else if (action.equalsIgnoreCase("quit")) {
            Swingy.getInstance().quit();
        } else if (action.equalsIgnoreCase("home")) {
            Swingy.getInstance().useSaveController();
        } else if (action.equalsIgnoreCase("help")) {
            // TODO Show the help
        }
    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu actionMenu = new JMenu("Action");
        actionMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(actionMenu);
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.setMnemonic(KeyEvent.VK_Q);
        quitMenuItem.getAccessibleContext().setAccessibleDescription("Quit the game");
        quitMenuItem.setActionCommand("quit");
        quitMenuItem.addActionListener(this);
        actionMenu.add(quitMenuItem);
        JMenuItem homeMenuItem = new JMenuItem("Home");
        homeMenuItem.setMnemonic(KeyEvent.VK_Q);
        homeMenuItem.getAccessibleContext().setAccessibleDescription("Go back to the hero selection");
        homeMenuItem.setActionCommand("home");
        homeMenuItem.addActionListener(this);
        actionMenu.add(homeMenuItem);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(viewMenu);
        JMenuItem consoleMenuItem = new JMenuItem("Console");
        consoleMenuItem.getAccessibleContext().setAccessibleDescription("Switch to Console view");
        consoleMenuItem.setActionCommand("change-display");
        consoleMenuItem.addActionListener(this);
        viewMenu.add(consoleMenuItem);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.getAccessibleContext().setAccessibleDescription("Show the help");
        helpMenu.setActionCommand("help");
        helpMenu.addActionListener(this);
        menuBar.add(helpMenu);

        return menuBar;
    }

    public UI() {
        frame = new JFrame("Swingy");
        frame.setJMenuBar(createMenuBar());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JLabel("Loading..."));
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        DimensionUIResource dimensions = new DimensionUIResource(480, 480);
        frame.setPreferredSize(dimensions);
        frame.setMinimumSize(dimensions);
        frame.setMaximumSize(dimensions);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.repaint();
    }

    public void show() {
        frame.setVisible(true);
    }

    public void repaint() {
        frame.pack();
        frame.repaint();
    }

    public void hide() {
        frame.setVisible(false);
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    public JFrame getFrame() {
        return frame;
    }
}
