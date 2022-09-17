package org.glagan.Controller;

import java.util.Random;

import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.Core.FightCharacter;
import org.glagan.Core.FightReport;
import org.glagan.Core.Game;
import org.glagan.Core.Input;
import org.glagan.Display.Display;
import org.glagan.Display.Mode;
import org.glagan.View.Encounter;
import org.glagan.View.Fight;
import org.glagan.View.Lose;
import org.glagan.View.RunFailure;
import org.glagan.View.RunSuccess;
import org.glagan.View.Win;

public class FightController extends Controller {
    public FightController(org.glagan.Core.Swingy swingy) {
        super(swingy);
    }

    @Override
    public void reset() {
    }

    protected String waitOrAskForInput(String message, String prefix) {
        if (Display.getDisplay().equals(Mode.CONSOLE)) {
            String input = Input.ask(message, prefix);
            return input;
        } else {
            // TODO wait for a variable to change inside a view
        }
        return null;
    }

    @Override
    public void run() {
        Game game = swingy.getGame();
        Hero hero = game.getHero();
        Enemy enemy = game.getCurrentEnemy();

        // Initial Encounter view where the player choose to fight or leave
        new Encounter(hero, enemy).render();
        String input = this.waitOrAskForInput("> [f]ight [r]run", null);
        if (handleGlobalCommand(input)) {
            return;
        }
        boolean doFight = true;
        if (input.equalsIgnoreCase("f") || input.equalsIgnoreCase("fight")) {
            doFight = true;
        } else if (input.equalsIgnoreCase("r") || input.equalsIgnoreCase("run")) {
            boolean runSuccess = new Random().nextBoolean();
            if (runSuccess) {
                game.setCurrentEnemy(null);
                game.save();
                new RunSuccess().render();
                this.waitOrAskForInput(null, "Press enter to go back");
                swingy.useGameController();
                return;
            } else {
                new RunFailure().render();
                doFight = true;
            }
        } else {
            System.out.println("Invalid command `" + input + "`");
        }

        // If the fight is happening, execute the logic
        if (doFight) {
            FightReport report = game.fightEnemy();
            new Fight(report).render();
            if (report.getWinner().equals(FightCharacter.PLAYER)) {
                new Win().render();
                if (game.getEnemyDrop() == null) {
                    this.waitOrAskForInput(null, "Press enter to go back");
                }
                swingy.useGameController();
            } else {
                new Lose().render();
                this.waitOrAskForInput(null, "Press enter to go to the main menu");
                swingy.useStartController();
            }
        }
    }

}
