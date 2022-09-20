package org.glagan.View;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

import org.glagan.Artefact.Artefact;
import org.glagan.Controller.Controller;
import org.glagan.Core.Caracteristics;
import org.glagan.Core.Input;
import org.glagan.Display.CurrentDisplay;
import org.glagan.Display.Mode;

import com.github.tomaslanger.chalk.Chalk;

import net.miginfocom.swing.MigLayout;

abstract public class View {
    protected Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    abstract public void console();

    abstract public void gui();

    final public void render() {
        Mode mode = CurrentDisplay.getMode();
        if (mode.equals(Mode.CONSOLE)) {
            this.console();
        } else {
            this.gui();
        }
    }

    protected boolean dispatch(String event) {
        return controller.handle(event);
    }

    protected boolean waitInputAndDispatch(String message, String prefix) {
        String input = Input.ask(message, prefix);
        return dispatch(input);
    }

    protected boolean waitInputAndLoopDispatch(String message, String prefix, String commandPrefix) {
        String input;
        do {
            input = Input.ask(message, prefix);
        } while (!dispatch(commandPrefix != null ? commandPrefix + " " + input : input));
        return true;
    }

    protected boolean waitGoBack(String message) {
        Input.ask(null, message != null ? message : "Press enter to go back");
        return dispatch("continue");
    }

    protected JPanel renderArtefactGui(Artefact artefact) {
        JPanel artefactPanel = new JPanel(new MigLayout("fillx, align center, insets 0"));
        artefactPanel.add(new JLabel(artefact.getName()), "span, center, wrap");
        JLabel artefactSlot = new JLabel();
        switch (artefact.getSlot()) {
            case HELM:
                artefactSlot.setText("Helmet");
                break;
            case ARMOR:
                artefactSlot.setText("Armor");
                break;
            case WEAPON:
                artefactSlot.setText("Weapon");
                break;
        }
        artefactPanel.add(artefactSlot, "span, center, wrap");

        JPanel caracteristicsPanel = new JPanel(new MigLayout("insets 0"));
        Caracteristics bonuses = artefact.getBonuses();
        if (bonuses.getAttack() != 0) {
            JPanel attackPanel = new JPanel(new MigLayout("fillx, insets 0"));
            if (bonuses.getAttack() > 0) {
                attackPanel.add(new JLabel("+"));
            }
            JLabel attack = new JLabel("" + bonuses.getAttack());
            attack.setForeground(new ColorUIResource(66, 200, 66));
            attackPanel.add(attack);
            attackPanel.add(new JLabel(" attack"));
            caracteristicsPanel.add(attackPanel, "span, wrap");
        }
        if (bonuses.getDefense() != 0) {
            JPanel defensePanel = new JPanel(new MigLayout("fillx, insets 0"));
            if (bonuses.getDefense() > 0) {
                defensePanel.add(new JLabel("+"));
            }
            JLabel defense = new JLabel("" + bonuses.getDefense());
            defense.setForeground(new ColorUIResource(66, 200, 66));
            defensePanel.add(defense);
            defensePanel.add(new JLabel(" defense"));
            caracteristicsPanel.add(defensePanel, "span, wrap");
        }
        if (bonuses.getHitPoints() != 0) {
            JPanel hpPanel = new JPanel(new MigLayout("fillx, insets 0"));
            if (bonuses.getHitPoints() > 0) {
                hpPanel.add(new JLabel("+"));
            }
            JLabel hitpoints = new JLabel("" + bonuses.getHitPoints());
            hitpoints.setForeground(new ColorUIResource(66, 200, 66));
            hpPanel.add(hitpoints);
            hpPanel.add(new JLabel(" hp"));
            caracteristicsPanel.add(hpPanel, "span, wrap");
        }
        artefactPanel.add(caracteristicsPanel, "span, wrap, center");

        return artefactPanel;
    }

    protected void printArtefactConsole(Artefact artefact) {
        switch (artefact.getSlot()) {
            case HELM:
                System.out.print("Helmet\t");
                break;
            case ARMOR:
                System.out.print("Armor\t");
                break;
            case WEAPON:
                System.out.print("Weapon\t");
                break;
        }
        System.out.print(Chalk.on(artefact.getName()).underline() + " ");
        switch (artefact.getRarity()) {
            case LEGENDARY:
                System.out.println("(" + Chalk.on("Legendary").yellow() + ")");
                break;
            case RARE:
                System.out.println("(" + Chalk.on("Rare").cyan() + ")");
                break;
            case COMMON:
                System.out.println("(Common)");
                break;
        }
        System.out.print("\t");
        Caracteristics bonuses = artefact.getBonuses();
        boolean wrote = false;
        if (bonuses.getAttack() != 0) {
            if (bonuses.getAttack() > 0) {
                System.out.print("+" + bonuses.getAttack());
            } else {
                System.out.print(bonuses.getAttack());
            }
            System.out.print(" attack");
            wrote = true;
        }
        if (bonuses.getDefense() != 0) {
            if (wrote) {
                System.out.print(", ");
            }
            if (bonuses.getDefense() > 0) {
                System.out.print("+" + bonuses.getDefense());
            } else {
                System.out.print(bonuses.getDefense());
            }
            System.out.print(" defense");
            wrote = true;
        }
        if (bonuses.getHitPoints() != 0) {
            if (wrote) {
                System.out.print(", ");
            }
            if (bonuses.getHitPoints() > 0) {
                System.out.print("+" + bonuses.getHitPoints());
            } else {
                System.out.print(bonuses.getHitPoints());
            }
            System.out.print(" hp");
            wrote = true;
        }
        System.out.println();
    }
}
