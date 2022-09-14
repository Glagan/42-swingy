package org.glagan.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import org.glagan.Core.Game;
import org.glagan.Core.Input;
import org.glagan.Core.Save;
import org.glagan.Display.Display;
import org.glagan.Display.Mode;
// import org.glagan.Core.Save;
import org.glagan.View.HeroCreation;
import org.glagan.View.SaveIndex;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

enum State {
    LIST,
    CREATE
}

public class SaveController extends Controller {
    protected State state;
    protected Integer selected;

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
        Integer index = 0;
        for (String path : files) {
            try {
                Reader reader = new FileReader(path);
                Game game = Game.deserialize(reader);
                saves[index] = new Save(path, game, false, null);
                // TODO validate valid json save values
            } catch (FileNotFoundException e) {
                saves[index] = new Save(path, null, true, "File not found");
            } catch (JsonSyntaxException e) {
                saves[index] = new Save(path, null, true, "Invalid syntax in save file: " + e.getMessage());
            } catch (JsonIOException e) {
                saves[index] = new Save(path, null, true, "Failed to read file: " + e.getMessage());
            } catch (Exception e) {
                saves[index] = new Save(path, null, true, "Failed to read file: " + e.getMessage());
            }
            index += 1;
        }

        return saves;
    }

    @Override
    public void reset() {
        this.state = State.LIST;
        this.selected = -1;
    }

    @Override
    public void run() {
        if (this.state.equals(State.LIST)) {
            this.saveIndex();
        } else {
            this.heroCreation();
        }
    }

    protected boolean waitOrAskForInput() {
        if (Display.getDisplay().equals(Mode.CONSOLE)) {
            while (true) {
                String input = Input.ask("> [s]elect {number} [c]reate", null);
                if (input == null) {
                    return false;
                }
                if (input.equalsIgnoreCase("c") || input.equalsIgnoreCase("create")) {
                    this.state = State.CREATE;
                    break;
                } else if (input.startsWith("s ") || input.startsWith("select ")) {
                    // TODO parse index and select
                } else {
                    System.out.println("Invalid command `" + input + "`");
                }
            }
        } else {
            // TODO wait for a variable to change inside a view
        }
        return true;
    }

    protected void saveIndex() {
        Save[] saves = this.getSaves();
        SaveIndex saveIndex = new SaveIndex(saves);
        saveIndex.render();
        if (!this.waitOrAskForInput()) {
            return;
        }
        // TODO waitOrAskForInput: ask for input / wait for input
        // TODO set state to State.CREATE on `create`
        // TODO redirect to Game Controller -> generate game and start (render map)
    }

    protected void heroCreation() {
        HeroCreation creationMenu = new HeroCreation();
        creationMenu.render();
        // TODO ask for input / wait for input
        // TODO create hero
        // TODO redirect to Game Controller -> generate game and start (render map)
    }
}
