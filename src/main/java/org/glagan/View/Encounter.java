package org.glagan.View;

import org.glagan.Controller.Controller;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.Core.Caracteristics;

import net.miginfocom.swing.MigLayout;

public class Encounter extends View implements ActionListener {
    protected Hero hero;
    protected Enemy enemy;

    public Encounter(Controller controller, Hero hero, Enemy enemy) {
        super(controller);
        this.hero = hero;
        this.enemy = enemy;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("fight")) {
            dispatch("fight");
        } else if (action.equals("run")) {
            dispatch("run");
        }
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new MigLayout("fillx"));

        panel.add(new JLabel("You encountered an enemy !"), "span, grow, wrap");
        panel.add(new JLabel(enemy.getName() + ", level " + enemy.getLevel()), "span, grow, wrap");
        Caracteristics caracteristics = enemy.getCaracteristics();
        panel.add(new JLabel(caracteristics.getAttack() + " attack, " + caracteristics.getDefense() + " defense, "
                + caracteristics.getHitPoints() + " hp"), "span, grow, wrap");
        panel.add(new JLabel("(You) " + hero.getName() + ", level " + hero.getLevel()), "span, grow, wrap");
        Caracteristics heroCaracteristics = hero.getFinalCaracteristics();
        panel.add(
                new JLabel(heroCaracteristics.getAttack() + " attack, " + heroCaracteristics.getDefense() + " defense, "
                        + heroCaracteristics.getHitPoints() + " hp"),
                "span, grow, wrap");

        JPanel actionPanel = new JPanel(new MigLayout("insets 0, fillx, center"));
        JButton fightButton = new JButton("Fight");
        fightButton.setMnemonic(KeyEvent.VK_F);
        fightButton.setActionCommand("fight");
        fightButton.addActionListener(this);
        fightButton.requestFocusInWindow();
        actionPanel.add(fightButton, "width 50%, center");
        JButton runButton = new JButton("Run");
        runButton.setMnemonic(KeyEvent.VK_R);
        runButton.setActionCommand("run");
        runButton.addActionListener(this);
        actionPanel.add(runButton, "width 50%, center");
        panel.add(actionPanel, "span, grow, center");

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void console() {
        System.out.println();
        System.out.println("You encountered an enemy !");
        System.out.println(enemy.getName() + ", level " + enemy.getLevel());
        Caracteristics caracteristics = enemy.getCaracteristics();
        System.out.println(caracteristics.getAttack() + " attack, " + caracteristics.getDefense() + " defense, "
                + caracteristics.getHitPoints() + " hp");
        System.out.println("(You) " + hero.getName() + ", level " + hero.getLevel());
        Caracteristics heroCaracteristics = hero.getFinalCaracteristics();
        System.out.println(heroCaracteristics.getAttack() + " attack, " + heroCaracteristics.getDefense() + " defense, "
                + heroCaracteristics.getHitPoints() + " hp");
        waitInputAndDispatch("> [f]ight [r]run", null);
    }
}
