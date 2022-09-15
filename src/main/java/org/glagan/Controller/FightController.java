package org.glagan.Controller;

import java.util.Random;

import org.glagan.Artefact.Artefact;
import org.glagan.Artefact.ArtefactSlot;
import org.glagan.Artefact.Rarity;
import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.Core.Caracteristics;
import org.glagan.Core.Game;
import org.glagan.Core.Input;
import org.glagan.Display.Display;
import org.glagan.Display.Mode;
import org.glagan.View.Encounter;
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

    protected boolean fightEnemy() {
        Game game = swingy.getGame();
        // Hero hero = game.getHero();
        // Enemy enemy = game.getCurrentEnemy();

        // TODO fight logic
        // TODO drop logic
        // TODO experience and level logic

        game.setCurrentEnemy(null);
        game.removeEnemiesAfterFight();
        game.save();

        return new Random().nextBoolean();
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
                new RunSuccess().render();
                this.waitOrAskForInput(null, "Press enter to go back");
                game.setCurrentEnemy(null);
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
            boolean win = fightEnemy();
            // TODO move logic to Game instead and only display/change controller here
            if (win) {
                new Win().render();
                Artefact artefact = null;
                boolean drop = new Random().nextBoolean();
                if (drop) {
                    artefact = new Artefact("Test", Rarity.COMMON, new Caracteristics(1, 1, 10), ArtefactSlot.HELM);
                    game.setEnemyDrop(artefact);
                    game.save();
                } else {
                    this.waitOrAskForInput(null, "Press enter to go back");
                }
                swingy.useGameController();
            } else {
                game.setMap(null);
                game.save();
                new Lose().render();
                swingy.useStartController();
            }
        }
    }

}
