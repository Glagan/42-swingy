package org.glagan.Controller;

import org.glagan.Artefact.Artefact;
import org.glagan.Character.Hero;
import org.glagan.Core.Game;
import org.glagan.Core.Input;
import org.glagan.Display.Display;
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

    @Override
    public void run() {
        // Check if the game has a map already generated
        Game game = swingy.getGame();

        // If there is a remaining drop, handle it before continuing
        if (game.getEnemyDrop() != null) {
            state = GameState.DROP;
        } else if (game.getMap() == null) {
            game.generateNewMap(null);
            game.save();
            state = GameState.MAP;
        }
        // If there is a fight in progress go to the fight controller instead
        else if (game.getCurrentEnemy() != null) {
            swingy.useFightController();
            return;
        } else {
            state = GameState.MAP;
        }

        // Execute the current state action
        if (this.state.equals(GameState.MAP)) {
            this.map();
        } else {
            this.artefactDrop();
        }
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

    protected void moveHeroInDirection(Direction direction) {
        Game game = swingy.getGame();
        if (game.moveHero(direction)) {
            swingy.useFightController();
        }
    }

    protected void map() {
        Game game = swingy.getGame();
        Hero hero = game.getHero();
        Map map = new Map(game.getMap(), hero);
        map.render();
        while (true) {
            String input = this.waitOrAskForInput("> [s]how [i]nventory [m]ove {[n]orth|[e]ast|[s]outh|[w]est}", null);
            if (handleGlobalCommand(input)) {
                return;
            }
            if (input.equalsIgnoreCase("s") || input.equalsIgnoreCase("show")) {
                return;
            } else if (input.equalsIgnoreCase("i") || input.equalsIgnoreCase("inventory")) {
                Inventory inventory = new Inventory(game.getMap(), hero);
                inventory.render();
                this.waitOrAskForInput(null, "Press enter to go back");
                return;
            } else if (input.equalsIgnoreCase("mn")) {
                moveHeroInDirection(Direction.NORTH);
                return;
            } else if (input.equalsIgnoreCase("me")) {
                moveHeroInDirection(Direction.EAST);
                return;
            } else if (input.equalsIgnoreCase("ms")) {
                moveHeroInDirection(Direction.SOUTH);
                return;
            } else if (input.equalsIgnoreCase("mw")) {
                moveHeroInDirection(Direction.WEST);
                return;
            } else if (input.startsWith("m ") || input.startsWith("move ")) {
                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    System.out.println("Invalid command `" + input + "`");
                } else {
                    String direction = parts[1];
                    if (direction.equalsIgnoreCase("n") || direction.equalsIgnoreCase("north")) {
                        moveHeroInDirection(Direction.NORTH);
                    } else if (direction.equalsIgnoreCase("e") || direction.equalsIgnoreCase("east")) {
                        moveHeroInDirection(Direction.EAST);
                    } else if (direction.equalsIgnoreCase("s") || direction.equalsIgnoreCase("south")) {
                        moveHeroInDirection(Direction.SOUTH);
                    } else if (direction.equalsIgnoreCase("w") || direction.equalsIgnoreCase("west")) {
                        moveHeroInDirection(Direction.WEST);
                    } else {
                        System.out
                                .println("Invalid direction `" + direction + "`, expected north, east, south or west");
                    }
                    return;
                }
            } else {
                System.out.println("Invalid command `" + input + "`");
            }
        }
    }

    protected void artefactDrop() {
        Game game = swingy.getGame();
        Artefact artefact = game.getEnemyDrop();
        new ArtefactDrop(game.getHero(), game.getEnemyDrop()).render();

        while (true) {
            String input = this.waitOrAskForInput("> [e]quip [l]eave", null);
            if (handleGlobalCommand(input)) {
                return;
            }
            if (input.equalsIgnoreCase("e") || input.equalsIgnoreCase("equip")) {
                game.getHero().equipArtefact(artefact);
                game.setEnemyDrop(null);
                game.save();
                return;
            } else if (input.equalsIgnoreCase("l") || input.equalsIgnoreCase("leave")) {
                game.setEnemyDrop(null);
                game.save();
                return;
            } else {
                System.out.println("Invalid command `" + input + "`");
            }
        }
    }
}
