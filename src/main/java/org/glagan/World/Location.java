package org.glagan.World;

import org.glagan.Character.Enemy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Location {
    @NotNull
    @Min(0)
    protected int x;

    @NotNull
    @Min(0)
    protected int y;

    @NotNull
    protected Biome biome;

    protected Enemy[] enemies;

    @NotNull
    protected boolean enemiesAreVisible;

    @NotNull
    protected boolean isVisible;

    public Location(@NotNull @Min(0) int x, @NotNull @Min(0) int y, @NotNull Biome biome, Enemy[] enemies,
            @NotNull boolean enemiesAreVisible, @NotNull boolean isVisible) {
        this.x = x;
        this.y = y;
        this.biome = biome;
        this.enemies = enemies;
        this.enemiesAreVisible = enemiesAreVisible;
        this.isVisible = isVisible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Biome getBiome() {
        return biome;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public boolean hasEnemies() {
        return enemies != null && enemies.length > 0;
    }

    public boolean isEnemiesAreVisible() {
        return enemiesAreVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }
}
