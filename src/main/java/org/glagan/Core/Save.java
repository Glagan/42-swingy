package org.glagan.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.glagan.Character.Hero;

public class Save {
    protected String path;

    protected Game game;

    protected boolean corrupted;

    protected String error;

    public Save(String path, Game game, boolean corrupted, String error) {
        this.path = path;
        this.game = game;
        this.corrupted = corrupted;
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isCorrupted() {
        return corrupted;
    }

    public void setCorrupted(boolean corrupted) {
        this.corrupted = corrupted;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean delete() {
        if (game.getId() > 0) {
            game.delete(); // Delete from the database
        }
        File file = new File(path);
        return file.delete(); // Delete the save file
    }

    static public boolean ensureSavesDirectoryExists() {
        File saves = new File("saves");
        if (!saves.exists()) {
            if (!saves.mkdir()) {
                System.out.println(
                        "Failed to created saves directory, check that you have enough space and the permissions to write in the current folder");
                return false;
            }
        }
        return true;
    }

    static public Save load(String path) {
        return null;
    }

    static public String[] listSaveFiles() {
        if (!Save.ensureSavesDirectoryExists()) {
            return null;
        }

        String[] paths;
        try (Stream<Path> walk = Files.walk(Paths.get("saves"))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .filter(f -> !f.endsWith(".json"))
                    .map(f -> f.toString()).collect(Collectors.toList());
            paths = new String[result.size()];
            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    paths[i] = result.get(i);
                }
            }
        } catch (IOException e) {
            return null;
        }

        return paths;
    }

    public String toString() {
        if (isCorrupted()) {
            return getPath() + ", Corrupted: " + getError();
        }
        Hero hero = getGame().getHero();
        return hero.getName() + ", " + hero.getClass().getSimpleName() + " level " + hero.getLevel();
    }
}
