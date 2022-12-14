package org.glagan.Controller;

import org.glagan.Character.Hero;
import org.glagan.Core.Game;
import org.glagan.Display.CurrentDisplay;
import org.glagan.Display.Mode;
import org.glagan.View.ArtefactDrop;
import org.glagan.View.Inventory;
import org.glagan.View.Map;
import org.glagan.World.Direction;

enum GameState {
    INIT,
    MAP,
    DROP,
}

public class GameController extends Controller {
    protected GameState state;

    public GameController(org.glagan.Core.Swingy swingy) {
        super(swingy);
        this.reset();
    }

    @Override
    public void reset() {
        this.state = GameState.INIT;

    }

    protected void moveHeroInDirection(Direction direction) {
        Game game = swingy.getGame();
        org.glagan.World.Map oldMap = game.getMap();
        if (game.moveHero(direction)) {
            swingy.useFightController();
        } else if (CurrentDisplay.getMode().equals(Mode.GUI) && oldMap != game.getMap()) {
            // Re-render the GUI on map change, to avoid keeping old references in the UI
            new Map(this, game.getMap(), game.getHero()).render();
        }
    }

    @Override
    public boolean handle(String event) {
        if (handleGlobalCommand(event)) {
            return true;
        }
        Game game = swingy.getGame();
        if (event.equalsIgnoreCase("s") || event.equalsIgnoreCase("show")) {
            if (game.getCurrentArtefact() != null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            if (CurrentDisplay.getMode().equals(Mode.GUI)) {
                new Map(this, game.getMap(), game.getHero()).render();
            }
            return true;
        } else if (event.equalsIgnoreCase("i") || event.equalsIgnoreCase("inventory")) {
            if (game.getCurrentArtefact() != null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            new Inventory(this, game.getMap(), game.getHero()).render();
            return true;
        } else if (event.equalsIgnoreCase("mn")) {
            if (game.getCurrentArtefact() != null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            moveHeroInDirection(Direction.NORTH);
            return true;
        } else if (event.equalsIgnoreCase("me")) {
            if (game.getCurrentArtefact() != null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            moveHeroInDirection(Direction.EAST);
            return true;
        } else if (event.equalsIgnoreCase("ms")) {
            if (game.getCurrentArtefact() != null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            moveHeroInDirection(Direction.SOUTH);
            return true;
        } else if (event.equalsIgnoreCase("mw")) {
            if (game.getCurrentArtefact() != null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            moveHeroInDirection(Direction.WEST);
            return true;
        } else if (event.startsWith("m ") || event.startsWith("move ")) {
            if (game.getCurrentArtefact() != null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            String[] parts = event.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid command `" + event + "`");
            } else {
                String direction = parts[1];
                if (direction.equalsIgnoreCase("n") || direction.equalsIgnoreCase("north")) {
                    moveHeroInDirection(Direction.NORTH);
                    return true;
                } else if (direction.equalsIgnoreCase("e") || direction.equalsIgnoreCase("east")) {
                    moveHeroInDirection(Direction.EAST);
                    return true;
                } else if (direction.equalsIgnoreCase("s") || direction.equalsIgnoreCase("south")) {
                    moveHeroInDirection(Direction.SOUTH);
                    return true;
                } else if (direction.equalsIgnoreCase("w") || direction.equalsIgnoreCase("west")) {
                    moveHeroInDirection(Direction.WEST);
                    return true;
                } else {
                    System.out
                            .println("Invalid direction `" + direction + "`, expected north, east, south or west");
                }
            }
        } else if (event.equalsIgnoreCase("e") || event.equalsIgnoreCase("equip")) {
            if (game.getCurrentArtefact() == null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            game.getHero().equipArtefact(game.getCurrentArtefact());
            game.setEnemyDrop(null);
            game.save();
            if (CurrentDisplay.getMode().equals(Mode.GUI)) {
                new Map(this, game.getMap(), game.getHero()).render();
            }
            return true;
        } else if (event.equalsIgnoreCase("l") || event.equalsIgnoreCase("leave")) {
            if (game.getCurrentArtefact() == null) {
                System.out.println("Invalid command `" + event + "`");
                return false;
            }
            game.setEnemyDrop(null);
            game.save();
            if (CurrentDisplay.getMode().equals(Mode.GUI)) {
                new Map(this, game.getMap(), game.getHero()).render();
            }
            return true;
        } else {
            System.out.println("Invalid command `" + event + "`");
        }
        return false;
    }

    @Override
    public void run() {
        Game game = swingy.getGame();
        Hero hero = game.getHero();

        if (game.getCurrentArtefact() != null) {
            new ArtefactDrop(this, game.getHero(), game.getCurrentArtefact()).render();
        } else if (game.getCurrentEnemy() != null) {
            swingy.useFightController();
        } else {
            if (game.getMap() == null) {
                game.generateNewMap(null);
                game.save();
            }
            new Map(this, game.getMap(), hero).render();
        }
    }
}
