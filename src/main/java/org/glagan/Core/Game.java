package org.glagan.Core;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.glagan.Adapters.GsonCustomBuilder;
import org.glagan.Artefact.Artefact;
import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.World.Coordinates;
import org.glagan.World.Direction;
import org.glagan.World.Map;
import org.glagan.World.MapGenerator;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class Game {
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
        // Set locations global visibility
        Coordinates heroPosition = hero.getPosition();
        int size = map.getSize();
        int xUp = Math.max(0, heroPosition.getX() - 2);
        int xDown = Math.min(size, heroPosition.getX() + 2);
        int yUp = Math.max(0, heroPosition.getY() - 2);
        int yDown = Math.min(size, heroPosition.getY() + 2);
        for (; xUp < xDown; xUp++) {
            for (int y = yUp; y < yDown; y++) {
                map.setPositionVisible(xUp, y);
            }
        }

        int x = heroPosition.getX();
        int y = heroPosition.getY();
        if (x > 0) {
            if (y > 0) {
                map.setEnemiesVisible(x - 1, y - 1);
            }
            map.setEnemiesVisible(x - 1, y);
            if (y < size - 1) {
                map.setEnemiesVisible(x - 1, y + 1);
            }
        }
        if (y > 0) {
            map.setEnemiesVisible(x, y - 1);
        }
        if (y < size - 1) {
            map.setEnemiesVisible(x, y + 1);
        }
        if (x < size - 1) {
            if (y > 0) {
                map.setEnemiesVisible(x + 1, y - 1);
            }
            map.setEnemiesVisible(x + 1, y);
            if (y < size - 1) {
                map.setEnemiesVisible(x + 1, y + 1);
            }
        }
    }

    public void moveHero(Direction direction) {
        Coordinates heroPosition = hero.getPosition();
        switch (direction) {
            case NORTH:
                if (heroPosition.getX() > 0) {
                    hero.move(direction);
                }
                break;
            case EAST:
                if (heroPosition.getY() < map.getSize() - 1) {
                    hero.move(direction);
                }
                break;
            case SOUTH:
                if (heroPosition.getX() < map.getSize() - 1) {
                    hero.move(direction);
                }
                break;
            case WEST:
                if (heroPosition.getY() > 0) {
                    hero.move(direction);
                }
                break;
        }
        updateVisibility();
        save();
    }

    public void removeEnemiesAfterFight() {
        Coordinates position = hero.getPosition();
        map.removeEnemies(position.getX(), position.getY());
        save();
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

    public void generateNewMap() {
        // Cleanup previous enemyDrop and currentEnemy if they were modified in the save
        this.enemyDrop = null;
        this.currentEnemy = null;
        this.map = MapGenerator.getGenerator().generate(hero.getLevel());
    }

    public String serialize() {
        Gson gson = GsonCustomBuilder.getBuilder().create();
        return gson.toJson(this);
    }

    static public Game deserialize(Reader reader) throws JsonSyntaxException, JsonIOException {
        Gson gson = GsonCustomBuilder.getBuilder().create();
        return gson.fromJson(reader, Game.class);
    }

    public void save() {
        if (savePath != null) {
            String content = this.serialize();
            try {
                Files.write(Paths.get(savePath), content.getBytes());
            } catch (IOException e) {
                System.out.println("Failed to save game: " + e.getMessage());
            }
        } else {
            System.out.println("Failed to save game, no save path was generated");
        }
    }
}
