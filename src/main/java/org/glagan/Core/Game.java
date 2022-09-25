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
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.glagan.Adapters.GsonCustomBuilder;
import org.glagan.Artefact.Artefact;
import org.glagan.Artefact.ArtefactGenerator;
import org.glagan.Artefact.ArtefactSlot;
import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.Character.HeroFactory;
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
    protected Artefact currentArtefact;

    @Valid
    protected Enemy currentEnemy;

    public Game(Hero hero, Map map, Artefact currentArtefact, Enemy currentEnemy) {
        this.hero = hero;
        this.map = map;
        this.currentArtefact = currentArtefact;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSavePath() {
        return savePath;
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

    public Artefact getCurrentArtefact() {
        return currentArtefact;
    }

    public void setEnemyDrop(Artefact currentArtefact) {
        this.currentArtefact = currentArtefact;
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
        // Cleanup previous currentArtefact and currentEnemy if they were modified in
        // the save
        currentArtefact = null;
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

    static public Game load(Connection connection, int id) throws SQLException {
        // Load the Hero first
        String sql = "SELECT name, save_path, experience, class, x, y FROM heroes WHERE id = ? LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            return null;
        }
        Hero hero = HeroFactory.newHero(resultSet.getString("class"), resultSet.getString("name"));
        if (hero == null) {
            return null;
        }
        hero.setExperience(resultSet.getInt("experience"));
        int heroX = resultSet.getInt("x");
        int heroY = resultSet.getInt("y");
        String savePath = resultSet.getString("save_path");
        // Load it's artefacts
        sql = "SELECT name, rarity, attack, defense, hitpoints, slot FROM hero_artefacts WHERE hero_id = ? LIMIT 1";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        resultSet = statement.executeQuery();
        List<ArtefactSlot> usedSlots = new ArrayList<>();
        while (resultSet.next()) {
            Artefact artefact = Artefact.fromResultSet(resultSet);
            if (artefact != null) {
                if (usedSlots.contains(artefact.getSlot())) {
                    System.out.println(
                            "Duplicate Artefact for slot " + artefact.getSlot() + " in Hero " + hero.getName());
                }
                hero.equipArtefact(artefact);
                usedSlots.add(artefact.getSlot());
            }
        }

        // Finalize the hero
        hero.calculateFinalCaracteristics();

        // Load current enemy
        sql = "SELECT name, rank, level, attack, defense, hitpoints FROM hero_current_enemies WHERE hero_id = ? LIMIT 1";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        resultSet = statement.executeQuery();
        Enemy currentEnemy = null;
        if (resultSet.next()) {
            currentEnemy = Enemy.fromResultSet(resultSet);
        }

        // Load current artefact drop
        sql = "SELECT name, rarity, attack, defense, hitpoints, slot FROM hero_current_artefact WHERE hero_id = ? LIMIT 1";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        resultSet = statement.executeQuery();
        Artefact currentArtefact = null;
        if (resultSet.next()) {
            currentArtefact = Artefact.fromResultSet(resultSet);
        }

        // Load Map
        sql = "SELECT id, name, level, size FROM hero_map WHERE hero_id = ? LIMIT 1";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        resultSet = statement.executeQuery();
        Map map = null;
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            int level = resultSet.getInt("level");
            int size = resultSet.getInt("size");
            int mapId = resultSet.getInt("id");
            boolean invalid = name == null || level < 1 || size < 1;
            List<Location> locations = new ArrayList<>();
            if (!invalid) {
                // Load and parse all locations
                sql = "SELECT id, x, y, biome, enemies_visible, visible FROM hero_map_locations WHERE hero_map_id = ? ORDER BY x, y";
                PreparedStatement selectStatement = connection.prepareStatement(sql);
                selectStatement.setInt(1, mapId);
                ResultSet locationsSet = selectStatement.executeQuery();
                while (locationsSet.next()) {
                    int locationId = locationsSet.getInt("id");
                    Location location = Location.fromResultSet(locationsSet);
                    if (location == null) {
                        invalid = true;
                        break;
                    } else {
                        sql = "SELECT name, rank, level, attack, defense, hitpoints FROM hero_map_location_enemies WHERE hero_map_location_id = ? LIMIT 1";
                        PreparedStatement enemyStatement = connection.prepareStatement(sql);
                        enemyStatement.setInt(1, locationId);
                        ResultSet enemySet = enemyStatement.executeQuery();
                        if (enemySet.next()) {
                            Enemy enemy = Enemy.fromResultSet(enemySet);
                            if (enemy != null) {
                                Enemy[] enemies = { enemy };
                                location.setEnemies(enemies);
                            }
                        }
                        locations.add(location);
                    }
                }

                // Validate locations
                if (locations.size() != (size * size)) {
                    invalid = true;
                } else {
                    for (int x = 0; x < size; x++) {
                        for (int y = 0; y < size; y++) {
                            Location location = locations.get(x * size + y);
                            if (location == null || location.getX() != x || location.getY() != y) {
                                invalid = true;
                                break;
                            }
                        }
                        if (invalid) {
                            break;
                        }
                    }
                }
            }

            // Delete invalid map
            if (invalid) {
                sql = "DELETE FROM hero_map WHERE hero_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(sql);
                deleteStatement.setInt(1, id);
                deleteStatement.executeUpdate();
            } else {
                Location[][] arrLocations = new Location[size][size];
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        Location location = locations.get(x * size + y);
                        arrLocations[x][y] = location;
                    }
                }
                map = new Map(name, level, size, arrLocations);
            }
        }
        if (map != null) {
            hero.setPosition(new Coordinates(heroX, heroY));
        }

        Game game = new Game(hero, map, currentArtefact, currentEnemy);
        game.setId(id);
        game.setSavePath(savePath);
        return game;
    }

    protected void saveHero(Connection connection) throws SQLException {
        // Check that the ID is valid
        if (id > 0) {
            String sql = "SELECT id FROM heroes WHERE id = ? LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            // If there is no results the ID is invalid and we will create a new entry
            if (!resultSet.next()) {
                id = 0;
            } else {
                id = Optional.of(resultSet.getInt("id")).get();
            }
        }

        String sql = null;
        boolean insert = false;
        if (id > 0) {
            sql = "UPDATE heroes SET name = ?, save_path = ?, experience = ?, class = ?, x = ?, y = ? WHERE id = ?";
        } else {
            sql = "INSERT INTO heroes(name, save_path, experience, class, x, y) VALUES (?, ?, ?, ?, ?, ?)";
            insert = true;
        }
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, hero.getName());
        statement.setString(2, savePath);
        statement.setLong(3, hero.getExperience());
        statement.setString(4, hero.className());
        Coordinates heroPosition = hero.getPosition();
        if (heroPosition != null) {
            statement.setInt(5, heroPosition.getX());
            statement.setInt(6, heroPosition.getY());
        } else {
            statement.setInt(5, 0);
            statement.setInt(6, 0);
        }
        if (!insert) {
            statement.setInt(7, id);
        }

        int inserted = statement.executeUpdate();
        if (insert && inserted > 0) {
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    id = Optional.of(resultSet.getInt(1)).get();
                }
            }
        }
    }

    protected void saveHeroArtefact(Connection connection, ArtefactSlot slot) throws SQLException {
        Artefact artefact = hero.getArtefactInSlot(slot);
        if (artefact == null) {
            String sql = "DELETE FROM hero_artefacts WHERE slot = ? AND hero_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(sql);
            deleteStatement.setString(1, slot.toString());
            deleteStatement.setInt(2, id);
            deleteStatement.executeUpdate();
            return;
        }

        String sql = "SELECT id FROM hero_artefacts WHERE hero_id = ? AND slot = ? LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.setString(2, slot.toString());

        int artefactId = 0;
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            artefactId = resultSet.getInt("id");
        }

        if (artefactId > 0) {
            sql = "UPDATE hero_artefacts SET name = ?, rarity = ?, attack = ?, defense = ?, hitpoints = ?, slot = ? WHERE id = ?";

            PreparedStatement insertStatement = connection.prepareStatement(sql);
            insertStatement.setString(1, artefact.getName());
            insertStatement.setString(2, artefact.getRarity().toString());
            insertStatement.setLong(3, artefact.getBonuses().getAttack());
            insertStatement.setLong(4, artefact.getBonuses().getDefense());
            insertStatement.setLong(5, artefact.getBonuses().getHitPoints());
            insertStatement.setString(6, slot.toString());
            insertStatement.setInt(7, artefactId);
            insertStatement.executeUpdate();
        } else {
            sql = "INSERT INTO hero_artefacts(hero_id, name, rarity, attack, defense, hitpoints, slot) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement insertStatement = connection.prepareStatement(sql);
            insertStatement.setInt(1, id);
            insertStatement.setString(2, artefact.getName());
            insertStatement.setString(3, artefact.getRarity().toString());
            insertStatement.setLong(4, artefact.getBonuses().getAttack());
            insertStatement.setLong(5, artefact.getBonuses().getDefense());
            insertStatement.setLong(6, artefact.getBonuses().getHitPoints());
            insertStatement.setString(7, slot.toString());
            insertStatement.executeUpdate();
        }
    }

    protected void saveHeroArtefacts(Connection connection) throws SQLException {
        saveHeroArtefact(connection, ArtefactSlot.HELM);
        saveHeroArtefact(connection, ArtefactSlot.ARMOR);
        saveHeroArtefact(connection, ArtefactSlot.WEAPON);
    }

    public void saveCurrentEnemy(Connection connection) throws SQLException {
        String sql = "DELETE FROM hero_current_enemies WHERE hero_id = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(sql);
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();

        if (currentEnemy != null) {
            Enemy enemy = currentEnemy;
            String insertSql = "INSERT INTO hero_current_enemies(hero_id, name, rank, level, attack, defense, hitpoints) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setInt(1, id);
            insertStatement.setString(2, enemy.getName());
            insertStatement.setString(3, enemy.getRank().toString());
            insertStatement.setInt(4, enemy.getLevel());
            Caracteristics caracteristics = enemy.getCaracteristics();
            insertStatement.setInt(5, caracteristics.getAttack());
            insertStatement.setInt(6, caracteristics.getDefense());
            insertStatement.setInt(7, caracteristics.getHitPoints());
            insertStatement.executeUpdate();
        }
    }

    public void saveCurrentDrop(Connection connection) throws SQLException {
        String sql = "DELETE FROM hero_current_artefact WHERE hero_id = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(sql);
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();

        if (currentArtefact != null) {
            Artefact artefact = currentArtefact;
            String insertSql = "INSERT INTO hero_current_artefact(hero_id, name, rarity, attack, defense, hitpoints, slot) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setInt(1, id);
            insertStatement.setString(2, artefact.getName());
            insertStatement.setString(3, artefact.getRarity().toString());
            Caracteristics caracteristics = artefact.getBonuses();
            insertStatement.setInt(4, caracteristics.getAttack());
            insertStatement.setInt(5, caracteristics.getDefense());
            insertStatement.setInt(6, caracteristics.getHitPoints());
            insertStatement.setString(7, artefact.getSlot().toString());
            insertStatement.executeUpdate();
        }
    }

    public void saveMap(Connection connection) throws SQLException {
        // Delete the map if there is none currently
        if (map == null) {
            String sql = "DELETE FROM hero_map WHERE hero_id = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(sql);
            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();
            return;
        }

        // Check if a map exists
        int mapId = 0;
        String sql = "SELECT id, name, level, size FROM hero_map WHERE hero_id = ? LIMIT 1";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        // If there is no results the ID is invalid and we will create a new entry
        boolean maybeHasLocations = false;
        if (resultSet.next()) {
            mapId = resultSet.getInt("id");
            if (!resultSet.getString("name").equals(map.getName()) || resultSet.getInt("level") != map.getLevel()
                    || resultSet.getInt("size") != map.getSize()) {
                // If the level or size doesn't match, update them
                sql = "UPDATE hero_map SET name = ?, level = ?, size = ? WHERE id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(sql);
                updateStatement.setString(1, map.getName());
                updateStatement.setInt(2, map.getLevel());
                updateStatement.setInt(3, map.getSize());
                updateStatement.setLong(4, mapId);
                updateStatement.executeUpdate();
                // -- and delete all locations
                sql = "DELETE FROM hero_map_locations WHERE hero_map_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(sql);
                deleteStatement.setInt(1, mapId);
                deleteStatement.executeUpdate();
            } else {
                maybeHasLocations = true;
            }
        } else {
            sql = "INSERT INTO hero_map(hero_id, name, level, size) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1, id);
            insertStatement.setString(2, map.getName());
            insertStatement.setInt(3, map.getLevel());
            insertStatement.setInt(4, map.getSize());
            int inserted = insertStatement.executeUpdate();
            if (inserted > 0) {
                try (ResultSet insertSet = insertStatement.getGeneratedKeys()) {
                    if (insertSet.next()) {
                        mapId = Optional.of(insertSet.getInt(1)).get();
                    }
                }
            }
        }

        // Check if the locations already exists to update them instead of re-creating
        Location[] existingLocations = null;
        Integer[] existingLocationIds = null;
        if (maybeHasLocations) {
            // Load and parse all locations
            sql = "SELECT id, x, y, biome, enemies_visible, visible FROM hero_map_locations WHERE hero_map_id = ? ORDER BY x, y";
            PreparedStatement selectStatement = connection.prepareStatement(sql);
            selectStatement.setInt(1, mapId);
            ResultSet locationsSet = selectStatement.executeQuery();
            List<Location> locations = new ArrayList<>();
            List<Integer> locationIds = new ArrayList<>();
            boolean deleteLocations = false;
            while (locationsSet.next()) {
                Location location = Location.fromResultSet(locationsSet);
                if (location == null) {
                    deleteLocations = true;
                    break;
                } else {
                    locations.add(location);
                    locationIds.add(locationsSet.getInt("id"));
                }
            }

            // Validate locations
            if (locations.size() != (map.getSize() * map.getSize())) {
                deleteLocations = true;
            } else {
                for (int x = 0; x < map.getSize(); x++) {
                    for (int y = 0; y < map.getSize(); y++) {
                        Location location = locations.get(x * map.getSize() + y);
                        if (location.getX() != x || location.getY() != y) {
                            deleteLocations = true;
                            break;
                        }
                    }
                }
            }

            // Delete them if they are invalid
            if (deleteLocations) {
                sql = "DELETE FROM hero_map_locations WHERE hero_map_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(sql);
                deleteStatement.setInt(1, mapId);
                deleteStatement.executeUpdate();
                return;
            } else {
                existingLocations = locations.toArray(new Location[locations.size()]);
                existingLocationIds = locationIds.toArray(new Integer[locationIds.size()]);
            }
        }

        // Save the map locations
        int index = 0;
        for (Location[] row : map.getLocations()) {
            for (Location location : row) {
                int locationId = 0;
                if (existingLocations != null) {
                    locationId = existingLocationIds[index];
                    sql = "UPDATE hero_map_locations SET biome = ?, enemies_visible = ?, visible = ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    updateStatement.setString(1, location.getBiome().toString());
                    updateStatement.setBoolean(2, location.isEnemiesAreVisible());
                    updateStatement.setBoolean(3, location.isVisible());
                    updateStatement.setInt(4, locationId);
                    updateStatement.executeUpdate();
                } else {
                    sql = "INSERT INTO hero_map_locations(hero_map_id, x, y, biome, enemies_visible, visible) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    insertStatement.setInt(1, mapId);
                    insertStatement.setInt(2, location.getX());
                    insertStatement.setInt(3, location.getY());
                    insertStatement.setString(4, location.getBiome().toString());
                    insertStatement.setBoolean(5, location.isEnemiesAreVisible());
                    insertStatement.setBoolean(6, location.isVisible());
                    int inserted = insertStatement.executeUpdate();
                    if (inserted > 0) {
                        try (ResultSet insertSet = insertStatement.getGeneratedKeys()) {
                            if (insertSet.next()) {
                                locationId = Optional.of(insertSet.getInt(1)).get();
                            }
                        }
                    }
                }
                if (location.hasEnemies()) {
                    Enemy enemy = location.getEnemies()[0];
                    sql = "INSERT INTO hero_map_location_enemies(hero_map_location_id, name, rank, level, attack, defense, hitpoints) VALUES (?, ?, ?, ?, ?, ?, ?) "
                            + "ON CONFLICT (hero_map_location_id) DO UPDATE SET name = excluded.name, rank = excluded.rank, level = excluded.level, attack = excluded.attack, defense = excluded.defense, hitpoints = excluded.hitpoints";
                    PreparedStatement insertStatement = connection.prepareStatement(sql);
                    insertStatement.setInt(1, locationId);
                    insertStatement.setString(2, enemy.getName());
                    insertStatement.setString(3, enemy.getRank().toString());
                    insertStatement.setInt(4, enemy.getLevel());
                    insertStatement.setInt(5, enemy.getCaracteristics().getAttack());
                    insertStatement.setInt(6, enemy.getCaracteristics().getDefense());
                    insertStatement.setInt(7, enemy.getCaracteristics().getHitPoints());
                    insertStatement.executeUpdate();
                } else {
                    sql = "DELETE FROM hero_map_location_enemies WHERE hero_map_location_id = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(sql);
                    deleteStatement.setInt(1, locationId);
                    deleteStatement.executeUpdate();
                }
                index += 1;
            }
        }
    }

    public boolean save(boolean... forceSave) {
        Database database = Swingy.getInstance().getDatabase();
        if (database != null && database.isConnected()) {
            Connection connection = database.getConnection();
            try {
                // Update main table
                saveHero(connection);
                saveHeroArtefacts(connection);

                // Update "current" objects
                saveCurrentEnemy(connection);
                saveCurrentDrop(connection);

                // Save Map
                saveMap(connection);

                return true;
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to save to the database: \u001B[0m" + e.getMessage());
                // e.printStackTrace();
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

    public void delete() {
        Database database = Swingy.getInstance().getDatabase();
        if (database != null && database.isConnected()) {
            Connection connection = database.getConnection();
            try {
                String sql = "DELETE FROM heroes WHERE id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(sql);
                deleteStatement.setInt(1, id);
                deleteStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to delete from the database: \u001B[0m" + e.getMessage());
                Swingy.getInstance().closeDatabase();
            }
        }
    }
}
