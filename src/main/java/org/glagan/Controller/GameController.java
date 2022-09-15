package org.glagan.Controller;

import org.glagan.Adapters.GsonCustomBuilder;
import org.glagan.Character.Hero;
import org.glagan.Core.Game;
import org.glagan.Core.Input;
import org.glagan.Display.Display;
import org.glagan.Display.Mode;
import org.glagan.View.Map;
import org.glagan.World.Coordinates;
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
            game.generateNewMap();
            int center = game.getMap().getSize() / 2;
            game.getHero().setPosition(new Coordinates(center, center));
            game.save();
            System.out.println(GsonCustomBuilder.getBuilder().create().toJson(game.getMap()));
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
            this.drop();
        }
    }

    protected String waitOrAskForInput(String message) {
        if (Display.getDisplay().equals(Mode.CONSOLE)) {
            String input = Input.ask(message != null ? message : "> action", null);
            return input;
        } else {
            // TODO wait for a variable to change inside a view
        }
        return null;
    }

    protected void map() {
        Game game = swingy.getGame();
        Hero hero = game.getHero();
        Map map = new Map(game.getMap(), hero);
        map.render();
        while (true) {
            String input = this.waitOrAskForInput("> [s]how [i]nventory [m]ove {north|east|south|west}");
            if (handleGlobalCommand(input)) {
                return;
            }
            if (input.equalsIgnoreCase("s") || input.equalsIgnoreCase("show")) {
                return;
            } else if (input.equalsIgnoreCase("i") || input.equalsIgnoreCase("inventory")) {
                // TODO show inventory and wait for "Enter" then reload
                return;
            } else if (input.startsWith("m ") || input.startsWith("move ")) {
                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    System.out.println("Invalid command `" + input + "`");
                } else {
                    String direction = parts[1];
                    if (direction.equalsIgnoreCase("n") || direction.equalsIgnoreCase("north")) {
                        game.moveHero(Direction.NORTH);
                        return;
                    } else if (direction.equalsIgnoreCase("e") || direction.equalsIgnoreCase("east")) {
                        game.moveHero(Direction.EAST);
                        return;
                    } else if (direction.equalsIgnoreCase("s") || direction.equalsIgnoreCase("south")) {
                        game.moveHero(Direction.SOUTH);
                        return;
                    } else if (direction.equalsIgnoreCase("w") || direction.equalsIgnoreCase("west")) {
                        game.moveHero(Direction.WEST);
                        return;
                    } else {
                        System.out
                                .println("Invalid direction `" + direction + "`, expected north, east, south or west");
                    }
                }
            } else {
                System.out.println("Invalid command `" + input + "`");
            }
        }
    }

    protected void drop() {
        Game game = swingy.getGame();
        System.out.println("drop: " + game.getEnemyDrop());
    }
}
