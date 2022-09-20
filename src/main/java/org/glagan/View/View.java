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

    protected void waitAndIgnore(String message) {
        Input.ask(null, message != null ? message : "Press enter to go back");
    }

    protected JPanel renderArtefactGui(Artefact artefact) {
        JPanel artefactPanel = new JPanel(new MigLayout("fillx, align center, insets 0"));
        artefactPanel.add(
                new JLabel("<html><body style=\"text-align: center;\">" + artefact.getName() + "</body></html>"),
                "span, center, wrap");
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

    protected String bonusString(int value, String prefix) {
        if (value != 0) {
            String out;
            if (value > 0) {
                out = "+" + value;
            } else {
                out = "" + value;
            }
            out = out + " " + prefix;
            return out;
        }
        return null;
    }

    protected void printSeparator(int length) {
        System.out.print("\t├");
        for (int i = 0; i < length + 2; i++) {
            System.out.print("─");
        }
        System.out.println("┤");
    }

    protected void printArtefactConsole(Artefact artefact) {
        Caracteristics bonuses = artefact.getBonuses();
        String attackBonus = bonusString(bonuses.getAttack(), "attack");
        String defenseBonus = bonusString(bonuses.getDefense(), "defense");
        String hpBonus = bonusString(bonuses.getHitPoints(), "hp");
        String fullName = artefact.getName();
        int fullNameDisplayLength = fullName.length();
        switch (artefact.getRarity()) {
            case LEGENDARY:
                fullName = fullName + " (" + Chalk.on("Legendary").yellow() + ")";
                fullNameDisplayLength += 12;
                break;
            case RARE:
                fullName = fullName + " (" + Chalk.on("Rare").cyan() + ")";
                fullNameDisplayLength += 7;
                break;
            case COMMON:
                fullName = fullName + " (Common)";
                fullNameDisplayLength += 9;
                break;
        }
        int longestString = fullNameDisplayLength;
        if (attackBonus != null) {
            longestString = Math.max(longestString, attackBonus.length());
        }
        if (defenseBonus != null) {
            longestString = Math.max(longestString, defenseBonus.length());
        }
        if (hpBonus != null) {
            longestString = Math.max(longestString, hpBonus.length());
        }

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

        System.out.print("╭");
        for (int i = 0; i < longestString + 2; i++) {
            System.out.print("─");
        }
        System.out.println("╮");
        String fill = new String(new char[longestString - fullNameDisplayLength]).replace("\0", " ");
        System.out.println("\t│ " + fullName + fill + " │");
        printSeparator(longestString);
        if (attackBonus != null) {
            fill = new String(new char[longestString - attackBonus.length()]).replace("\0", " ");
            System.out.println("\t│ " + attackBonus + fill + " │");
        }
        if (defenseBonus != null) {
            fill = new String(new char[longestString - defenseBonus.length()]).replace("\0", " ");
            System.out.println("\t│ " + defenseBonus + fill + " │");
        }
        if (hpBonus != null) {
            fill = new String(new char[longestString - hpBonus.length()]).replace("\0", " ");
            System.out.println("\t│ " + hpBonus + fill + " │");
        }
        System.out.print("\t╰");
        for (int i = 0; i < longestString + 2; i++) {
            System.out.print("─");
        }
        System.out.println("╯");
    }
}
