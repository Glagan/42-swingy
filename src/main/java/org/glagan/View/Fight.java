package org.glagan.View;

import org.glagan.Controller.Controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.plaf.ColorUIResource;

import org.glagan.Character.Hero;
import org.glagan.Core.FightCharacter;
import org.glagan.Core.FightReport;

import net.miginfocom.swing.MigLayout;

public class Fight extends View implements ActionListener {
    protected FightReport report;
    protected Hero hero;

    public Fight(Controller controller, FightReport report, Hero hero) {
        super(controller);
        this.report = report;
        this.hero = hero;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("continue")) {
            dispatch("continue");
        } else if (action.equals("home")) {
            dispatch("home");
        }
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new MigLayout("fillx"));

        panel.add(new JLabel("Fight result"), "span, wrap");

        JTextArea fightLogs = new JTextArea();
        fightLogs.setLineWrap(true);
        fightLogs.setRows(10);
        fightLogs.setWrapStyleWord(true);
        fightLogs.setEditable(false);
        int last = report.getLogs().size() - 1;
        for (int i = 0; i <= last; i++) {
            fightLogs.append(report.getLogs().get(i));
            if (i < last) {
                fightLogs.append("\n");
            }
        }
        fightLogs.setCaretPosition(fightLogs.getDocument().getLength());
        JScrollPane fightLogsScroll = new JScrollPane(fightLogs);
        panel.add(fightLogsScroll, "span, wrap, grow");

        if (report.getWinner().equals(FightCharacter.PLAYER)) {
            JLabel victoryText = new JLabel("You won the fight against your opponent !");
            victoryText.setForeground(new ColorUIResource(66, 180, 66));
            panel.add(victoryText, "span, wrap");

            JPanel experiencePanel = new JPanel(new MigLayout("fillx, insets 0"));
            experiencePanel.add(new JLabel("You gained "));
            JLabel experienceGained = new JLabel("" + report.getExperience());
            experienceGained.setForeground(new ColorUIResource(66, 66, 220));
            experiencePanel.add(experienceGained);
            experiencePanel.add(new JLabel(" experience points during the fight"), "wrap");
            panel.add(experiencePanel, "span, wrap");

            if (report.leveledUp()) {
                JPanel levelUpPanel = new JPanel(new MigLayout("fillx, insets 0"));
                levelUpPanel.add(new JLabel("You are now level "));
                JLabel levelUp = new JLabel("" + hero.getLevel());
                levelUp.setForeground(new ColorUIResource(66, 66, 220));
                levelUpPanel.add(levelUp, "wrap");
                panel.add(levelUpPanel, "span, wrap, gap 0");
            }
        } else {
            panel.add(new JLabel("After a tough fight, you sadly didn't manage to win against your"), "span, wrap");
            panel.add(new JLabel("opponent..."), "span, wrap");
            panel.add(new JLabel("A bright white light shine above your body before it disappears."), "span, wrap");
        }

        JPanel actionPanel = new JPanel(new MigLayout("fillx, align center, insets 0"));
        boolean playerWon = report.getWinner().equals(FightCharacter.PLAYER);
        JButton backButton = new JButton(playerWon ? "Back" : "Back to main menu");
        backButton.setActionCommand(playerWon ? "continue" : "home");
        backButton.addActionListener(this);
        actionPanel.add(backButton, "span, center");
        panel.add(actionPanel, "span, wrap, grow, center, gap 0");

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void console() {
        System.out.println();
        System.out.println("Fight logs:");
        for (String log : report.getLogs()) {
            System.out.println("  " + log);
        }

        if (report.getWinner().equals(FightCharacter.PLAYER)) {
            System.out.println("You won the fight against your opponent !");
            System.out.println("You gained " + report.getExperience() + " experience points during the fight");
            if (report.leveledUp()) {
                System.out.println("You are now level " + hero.getLevel());
            }
        } else {
            System.out.println("After a tough fight, you sadly didn't manage to win against your opponent...");
            System.out.println("A bright white light shine above your body before it disappears.");
            System.out.println();
        }

        if (report.getWinner().equals(FightCharacter.PLAYER)) {
            if (!report.hasDrop()) {
                waitGoBack("Press enter to go back");
            } else {
                dispatch("continue");
            }
        } else {
            waitGoBack("Press enter to go back to the main menu");
        }
    }
}
