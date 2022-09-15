package org.glagan.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Set;

import org.glagan.Character.Hero;
import org.glagan.Character.HeroFactory;
import org.glagan.Core.Game;
import org.glagan.Core.Input;
import org.glagan.Core.Save;
import org.glagan.Display.Display;
import org.glagan.Display.Mode;
import org.glagan.View.HeroCreation;
import org.glagan.View.SaveIndex;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

enum SaveState {
    LIST,
    CREATE
}

public class SaveController extends Controller {
    protected SaveState state;
    protected int selected;

    public SaveController(org.glagan.Core.Swingy swingy) {
        super(swingy);
        this.reset();
    }

    public Save[] getSaves() {
        String[] files = Save.listSaveFiles();
        if (files == null) {
            return null;
        }

        Save[] saves = new Save[files.length];
        int index = 0;
        for (String path : files) {
            try {
                Reader reader = new FileReader(path);
                Game game = Game.deserialize(reader);
                game.setSavePath(path);
                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                Set<ConstraintViolation<Game>> constraintViolations = validator.validate(game);
                if (constraintViolations.size() > 0) {
                    ConstraintViolation<Game> nextError = constraintViolations.iterator().next();
                    saves[index] = new Save(path, game, true,
                            "Validation failed: " + nextError.getPropertyPath() + " " + nextError.getMessage());
                } else if (game.getMap() != null && !game.getMap().validateLocations()) {
                    saves[index] = new Save(path, game, true, "The map doesn't have valid locations for it's size");
                } else {
                    saves[index] = new Save(path, game, false, null);
                }
            } catch (FileNotFoundException e) {
                saves[index] = new Save(path, null, true, "File not found");
            } catch (JsonSyntaxException e) {
                saves[index] = new Save(path, null, true, "Invalid syntax in save file: " + e.getMessage());
            } catch (JsonIOException e) {
                saves[index] = new Save(path, null, true, "Failed to read file: " + e.getMessage());
            }
            index += 1;
        }

        return saves;
    }

    @Override
    public void reset() {
        this.state = SaveState.LIST;
        this.selected = -1;
    }

    @Override
    public void run() {
        if (this.state.equals(SaveState.LIST)) {
            this.saveIndex();
        } else {
            this.heroCreation();
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

    protected void saveIndex() {
        Save[] saves = this.getSaves();
        new SaveIndex(saves).render();
        int saveId = -1;
        while (saveId < 1) {
            String input = this.waitOrAskForInput("> [s]elect {number} [c]reate [l]ist [d]elete {number}");
            if (handleGlobalCommand(input)) {
                return;
            }
            if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("create")) {
                this.state = SaveState.CREATE;
                break;
            } else if (input.startsWith("s ") || input.startsWith("select ")) {
                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    System.out.println("Invalid command `" + input + "`");
                } else {
                    try {
                        int id = Integer.parseInt(parts[1]);
                        if (id <= 0) {
                            System.out.println("Invalid selected hero `" + id + "`");
                        } else {
                            saveId = id;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid selected hero `" + parts[1] + "`");
                    }
                }
            } else if (input.equalsIgnoreCase("l") || input.equalsIgnoreCase("list")) {
                return;
            } else if (input.startsWith("d ") || input.startsWith("delete ")) {
                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    System.out.println("Invalid command `" + input + "`");
                } else {
                    try {
                        int id = Integer.parseInt(parts[1]);
                        if (id <= 0) {
                            System.out.println("Invalid selected hero `" + id + "`");
                        } else if (id <= 0 || id > saves.length) {
                            System.out.println("The selected save doesn't exists");
                        } else {
                            Save save = saves[id - 1];
                            System.out.println("Deleting " + save.getPath());
                            save.delete();
                            return;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid selected hero `" + parts[1] + "`");
                    }
                }
            } else {
                System.out.println("Invalid command `" + input + "`");
            }
        }
        if (saveId > 0 && saveId - 1 < saves.length) {
            Save save = saves[saveId - 1];
            if (save.isCorrupted()) {
                System.out.println("The selected save is corrupted and can't be played");
            } else {
                System.out.println("Playing as " + save.getGame().getHero().getName());
                swingy.setGame(save.getGame());
                swingy.useGameController();
            }
        } else {
            System.out.println("The selected save doesn't exists");
        }
    }

    protected void heroCreation() {
        new HeroCreation().render();

        int createState = 0;
        String name = null;
        Hero hero = null;
        while (createState < 2) {
            String input;
            switch (createState) {
                case 0:
                    input = Input.ask("> Name", "= ");
                    if (input != null && input.length() > 0) {
                        name = input;
                        createState++;
                    } else {
                        System.out.println("You should enter a name for your hero");
                    }
                    break;
                case 1:
                    input = Input.ask("> Class (Magician, Paladin, Warrior)", "= ");
                    hero = HeroFactory.newHero(input, name);
                    if (hero != null) {
                        hero.initializeCaracteristics();
                        createState++;
                    } else {
                        System.out.println("Invalid class");
                    }
                    break;
            }
        }
        if (hero != null) {
            Game game = new Game(hero, null, null, null);
            game.generateSavePath();
            game.save();
            swingy.setGame(game);
            swingy.useGameController();
        }
    }
}
