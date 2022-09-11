package org.glagan.Core;

import org.glagan.Artefact.Artefact;
import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.Map.Map;

import com.google.gson.Gson;

import jakarta.validation.constraints.NotNull;

public class Game {
    @NotNull
    protected String savePath;

    @NotNull
    protected Hero hero;

    @NotNull
    protected Map map;

    protected Artefact enemyDrop;

    protected Enemy currentEnemy;

    public Game(Hero hero, Map map, Artefact enemyDrop, Enemy currentEnemy) {
        this.hero = hero;
        this.map = map;
        this.enemyDrop = enemyDrop;
        this.currentEnemy = currentEnemy;
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
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
