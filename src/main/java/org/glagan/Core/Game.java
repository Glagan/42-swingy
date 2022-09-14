package org.glagan.Core;

import java.io.Reader;

import org.glagan.Adapters.GsonCustomBuilder;
import org.glagan.Artefact.Artefact;
import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.World.Map;

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

    @NotNull
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

    public String serialize() {
        Gson gson = GsonCustomBuilder.getBuilder().create();
        return gson.toJson(this);
    }

    static public Game deserialize(Reader reader) throws JsonSyntaxException, JsonIOException {
        Gson gson = GsonCustomBuilder.getBuilder().create();
        return gson.fromJson(reader, Game.class);
    }
}
