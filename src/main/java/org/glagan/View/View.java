package org.glagan.View;

import org.glagan.Artefact.Artefact;
import org.glagan.Controller.Controller;
import org.glagan.Core.Caracteristics;
import org.glagan.Core.Input;
import org.glagan.Display.CurrentDisplay;
import org.glagan.Display.Mode;

import com.github.tomaslanger.chalk.Chalk;

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

    protected void printArtefact(Artefact artefact) {
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
