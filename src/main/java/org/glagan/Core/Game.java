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
