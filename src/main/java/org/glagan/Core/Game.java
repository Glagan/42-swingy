package org.glagan.Core;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import org.glagan.Adapters.GsonCustomBuilder;
import org.glagan.Artefact.Artefact;
import org.glagan.Artefact.ArtefactGenerator;
import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.World.Coordinates;
import org.glagan.World.Direction;
import org.glagan.World.Location;
import org.glagan.World.Map;
import org.glagan.World.MapGenerator;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Game {
    @Min(0)
    protected int id;

    @NotNull
    protected String savePath;

    @NotNull
    @Valid
    protected Hero hero;

    @Valid
    protected Map map;

    @Valid
    protected Artefact enemyDrop;

    @Valid
    protected Enemy currentEnemy;

    public Game(Hero hero, Map map, Artefact enemyDrop, Enemy currentEnemy) {
        this.hero = hero;
        this.map = map;
        this.enemyDrop = enemyDrop;
        this.currentEnemy = currentEnemy;
    }

    public void generateSavePath() {
        Random rand = new Random();
        String name = this.hero.getName() + "_" + this.hero.className();
        Path path = Paths.get("saves/" + name + ".json");
        while (Files.exists(path)) {
            name = name + "_" + rand.nextInt(1000);
            path = Paths.get("saves/" + name + ".json");
        }
        this.savePath = path.toString();
    }

    public void setSavePath(String path) {
        this.savePath = path;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void updateVisibility() {
        // TODO merge both loops, use the increment + current position instead of
        // TODO -- incrementing a position

        // Set locations global visibility
        Coordinates heroPosition = hero.getPosition();
        int size = map.getSize();
        int xUp = Math.max(0, heroPosition.getX() - 2);
        int xDown = Math.min(size, heroPosition.getX() + 2);
        int yUp = Math.max(0, heroPosition.getY() - 2);
        int yDown = Math.min(size, heroPosition.getY() + 2);
        for (; xUp <= xDown; xUp++) {
            for (int y = yUp; y <= yDown; y++) {
                if (xUp >= 0 && xUp < size && y >= 0 && y < size) {
                    map.setPositionVisible(xUp, y);
                }
            }
        }
        // Set enemies visibility
        xUp = Math.max(0, heroPosition.getX() - 1);
        xDown = Math.min(size, heroPosition.getX() + 1);
        yUp = Math.max(0, heroPosition.getY() - 1);
        yDown = Math.min(size, heroPosition.getY() + 1);
        for (; xUp <= xDown; xUp++) {
            for (int y = yUp; y <= yDown; y++) {
                if (xUp >= 0 && xUp < size && y >= 0 && y < size) {
                    map.setEnemiesVisible(xUp, y);
                }
            }
        }
    }

    // Return true if an enemy was encountered or else false
    public boolean moveHero(Direction direction) {
        Coordinates heroPosition = hero.getPosition();
        boolean borderReached = false;
        switch (direction) {
            case NORTH:
                if (heroPosition.getX() > 0) {
                    hero.move(direction);
                } else {
                    borderReached = true;
                }
                break;
            case EAST:
                if (heroPosition.getY() < map.getSize() - 1) {
                    hero.move(direction);
                } else {
                    borderReached = true;
                }
                break;
            case SOUTH:
                if (heroPosition.getX() < map.getSize() - 1) {
                    hero.move(direction);
                } else {
                    borderReached = true;
                }
                break;
            case WEST:
                if (heroPosition.getY() > 0) {
                    hero.move(direction);
                } else {
                    borderReached = true;
                }
                break;
        }

        if (!borderReached) {
            updateVisibility();
            // Handle enemies encounter
            Location newLocation = map.getLocations()[heroPosition.getX()][heroPosition.getY()];
            if (newLocation.hasEnemies()) {
                setCurrentEnemy(newLocation.getEnemies()[0]);
                save();
                return true;
            } else {
                save();
            }
        } else {
            generateNewMap(direction);
            save();
        }

        return false;
    }

    public Artefact getEnemyDrop() {
        return enemyDrop;
    }

    public void setEnemyDrop(Artefact enemyDrop) {
        this.enemyDrop = enemyDrop;
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public void setCurrentEnemy(Enemy currentEnemy) {
        this.currentEnemy = currentEnemy;
    }

    /**
     * Generate a new random map and assign it to the game.
     *
     * @param fromDirection
     */
    public void generateNewMap(Direction fromDirection) {
        // Cleanup previous enemyDrop and currentEnemy if they were modified in the save
        enemyDrop = null;
        currentEnemy = null;
        map = MapGenerator.getGenerator().generate(hero.getLevel());
        int center = map.getSize() / 2;
        Coordinates start = new Coordinates(center, center);
        hero.setPosition(start);
        updateVisibility();
    }

    public FightReport fightEnemy() {
        Random rand = new Random();
        FightReport report = new FightReport();
        ArrayList<String> logs = new ArrayList<String>();

        FightCharacter character = rand.nextBoolean() ? FightCharacter.PLAYER : FightCharacter.ENEMY;
        Caracteristics enemyCaracteristics = currentEnemy.getCaracteristics().clone();
        Caracteristics heroCaracteristics = hero.getFinalCaracteristics().clone();
        while (true) {
            if (character.equals(FightCharacter.PLAYER)) {
                int damage = Math.max(1, heroCaracteristics.getAttack() - enemyCaracteristics.getDefense());
                logs.add("You deal " + damage + " (" + heroCaracteristics.getAttack() + "-"
                        + enemyCaracteristics.getDefense() + ") damage to " + currentEnemy.getName() + " ("
                        + enemyCaracteristics.getHitPoints() + "hp remaining)");
                enemyCaracteristics.setHitPoints(enemyCaracteristics.getHitPoints() - damage);
                if (enemyCaracteristics.getHitPoints() <= 0) {
                    logs.add("You killed " + currentEnemy.getName() + " !");
                    report.setWinner(character);
                    break;
                }
                character = FightCharacter.ENEMY;
            } else {
                int damage = Math.max(1, enemyCaracteristics.getAttack() - heroCaracteristics.getDefense());
                logs.add(currentEnemy.getName() + " deals " + damage + " (" + enemyCaracteristics.getAttack() + "-"
                        + heroCaracteristics.getDefense() + ") damage to you (" + heroCaracteristics.getHitPoints()
                        + "hp remaining)");
                heroCaracteristics.setHitPoints(heroCaracteristics.getHitPoints() - damage);
                if (heroCaracteristics.getHitPoints() <= 0) {
                    logs.add(currentEnemy.getName() + " killed you !");
                    report.setWinner(character);
                    break;
                }
                character = FightCharacter.PLAYER;
            }
        }
        report.logs = logs;

        int enemyLevel = currentEnemy.getLevel();
        setCurrentEnemy(null);
        Coordinates position = hero.getPosition();
        map.removeEnemies(position.getX(), position.getY());
        save();

        if (report.winner.equals(FightCharacter.PLAYER)) {
            // Drop random artefacts
            if (new Random().nextBoolean()) {
                setEnemyDrop(ArtefactGenerator.getGenerator().generate(enemyLevel));
                report.setHasDrop(true);
            }
            // Gain experience and update the level accordingly
            int experience = (enemyLevel * 500) + rand.nextInt(500) - 250;
            report.setExperience(experience);
            report.setLeveledUp(hero.addExperience(experience));
            save();
        } else {
            setMap(null);
            save();
        }

        return report;
    }

    public String serialize() {
        Gson gson = GsonCustomBuilder.getBuilder().create();
        return gson.toJson(this);
    }

    static public Game deserialize(Reader reader) throws JsonSyntaxException, JsonIOException {
        Gson gson = GsonCustomBuilder.getBuilder().create();
        return gson.fromJson(reader, Game.class);
    }

    public boolean save(boolean... forceSave) {
        Database database = Swingy.getInstance().getDatabase();
        if (database != null && database.isConnected()) {
            Connection connection = database.getConnection();
            try {
                // Update main table
                String heroSql = null;
                boolean insert = false;
                if (this.id > 0) {
                    heroSql = "UPDATE heroes SET name = ?, save_path = ?, experience = ?, class = ? WHERE id = ?";
                } else {
                    heroSql = "INSERT INTO heroes(name, save_path, experience, class) VALUES (?, ?, ?, ?)";
                    insert = true;
                }
                PreparedStatement statement = connection.prepareStatement(heroSql, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, hero.getName());
                statement.setString(2, savePath);
                statement.setLong(3, hero.getExperience());
                statement.setString(4, hero.className());
                if (!insert) {
                    statement.setInt(5, id);
                }

                int inserted = statement.executeUpdate();
                if (insert) {
                    if (inserted > 0) {
                        try (ResultSet resultSet = statement.getGeneratedKeys()) {
                            if (resultSet.next()) {
                                id = Optional.of(resultSet.getInt(1)).get();
                            }
                        }
                    }
                }

                return true;
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to save to the database: \u001B[0m" + e.getMessage());
                Swingy.getInstance().closeDatabase();
            }
            if (forceSave.length > 0 && forceSave[0]) {
                return false;
            }
        }
        if (savePath != null) {
            String content = this.serialize();
            try {
                Files.write(Paths.get(savePath), content.getBytes());
                return true;
            } catch (IOException e) {
                System.out.println("Failed to save game: " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Failed to save game, no save path was generated");
            return false;
        }
    }
}
