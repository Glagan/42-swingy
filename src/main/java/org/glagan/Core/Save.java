package org.glagan.Core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.glagan.Character.Hero;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

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

    static public Save[] all() {
        List<Save> saves = new ArrayList<>();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        List<Game> games = new ArrayList<>();

        // Load database saves
        Database database = Swingy.getInstance().getDatabase();
        if (database != null && database.isConnected()) {
            try {
                Connection connection = database.getConnection();
                String sql = "SELECT id FROM heroes ORDER BY id asc";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Game game = Game.load(connection, resultSet.getInt("id"));
                    if (game != null) {
                        games.add(game);
                    }
                }
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to get heroes from the database: \u001B[0m" + e.getMessage());
                e.printStackTrace();
                Swingy.getInstance().closeDatabase();
            }
        }

        // Load file saves
        String[] saveFiles = listSaveFiles();
        if (saveFiles != null && saveFiles.length > 0) {
            for (String path : saveFiles) {
                try {
                    Reader reader = new FileReader(path);
                    Game game = Game.deserialize(reader);
                    game.setSavePath(path);
                    games.add(game);
                } catch (FileNotFoundException e) {
                    saves.add(new Save(path, null, true, "File not found"));
                } catch (JsonSyntaxException e) {
                    saves.add(new Save(path, null, true, "Invalid syntax in save file: " + e.getMessage()));
                } catch (JsonIOException e) {
                    saves.add(new Save(path, null, true, "Failed to read file: " + e.getMessage()));
                }
            }
        }

        // Validate all loaded games
        for (Game game : games) {
            Set<ConstraintViolation<Game>> constraintViolations = validator.validate(game);
            boolean add = false;
            if (constraintViolations.size() > 0) {
                ConstraintViolation<Game> nextError = constraintViolations.iterator().next();
                saves.add(new Save(game.getSavePath(), game, true,
                        "Validation failed: " + nextError.getPropertyPath() + " " + nextError.getMessage()));
            } else if (game.getMap() != null) {
                if (!game.getMap().validateLocations()) {
                    saves.add(new Save(game.getSavePath(), game, true,
                            "The map doesn't have valid locations for it's size"));
                } else if (game.getHero().getPosition() == null) {
                    saves.add(new Save(game.getSavePath(), game, true, "The hero is not in the map"));
                } else if (game.getHero().getPosition().getX() >= game.getMap().getSize()
                        || game.getHero().getPosition().getY() >= game.getMap().getSize()) {
                    saves.add(new Save(game.getSavePath(), game, true, "The hero is outside of the map"));
                } else {
                    add = true;
                }
            } else {
                add = true;
            }
            if (add) {
                saves.add(new Save(game.getSavePath(), game, false, null));
            }
        }

        return saves.toArray(new Save[saves.size()]);
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
