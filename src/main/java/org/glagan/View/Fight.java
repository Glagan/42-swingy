package org.glagan.View;

import org.glagan.Character.Hero;
import org.glagan.Core.FightCharacter;
import org.glagan.Core.FightReport;

import org.glagan.Controller.Controller;

public class Fight extends View {
    protected FightReport report;
    protected Hero hero;

    public Fight(Controller controller, FightReport report, Hero hero) {
        super(controller);
        this.report = report;
        this.hero = hero;
    }

    @Override
    public void gui() {
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
            System.out.println("You gained " + report.getExperience() + " experience during the fight");
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
            }
        } else {
            waitGoBack("Press enter to go back to the main menu");
        }
    }
}
