package org.glagan.Controller;

import java.util.Random;

import org.glagan.Core.FightReport;
import org.glagan.Core.Game;
import org.glagan.View.Encounter;
import org.glagan.View.Fight;
import org.glagan.View.Run;

public class FightController extends Controller {
    public FightController(org.glagan.Core.Swingy swingy) {
        super(swingy);
    }

    @Override
    public void reset() {
    }

    protected void fightEnemy() {
        Game game = swingy.getGame();
        FightReport report = game.fightEnemy();
        new Fight(this, report, game.getHero()).render();
    }

    @Override
    public boolean handle(String event) {
        if (handleGlobalCommand(event)) {
            return true;
        }

        Game game = swingy.getGame();
        if (event.equalsIgnoreCase("f") || event.equalsIgnoreCase("fight")) {
            fightEnemy();
            return true;
        } else if (event.equalsIgnoreCase("r") || event.equalsIgnoreCase("run")) {
            boolean runSuccess = new Random().nextBoolean();
            if (runSuccess) {
                game.setCurrentEnemy(null);
                game.save();
                new Run(this, true).render();
            } else {
                new Run(this, false).render();
            }
            return true;
        } else if (event.equalsIgnoreCase("continue")) {
            swingy.useGameController();
            return true;
        } else {
            System.out.println("Invalid command `" + event + "`");
        }
        return false;
    }

    @Override
    public void run() {
        Game game = swingy.getGame();
        new Encounter(this, game.getHero(), game.getCurrentEnemy()).render();
    }

}
