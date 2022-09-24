package org.glagan.World;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.glagan.Character.Enemy;

import com.github.tomaslanger.chalk.Chalk;

import jakarta.validation.Valid;
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

    @Valid
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

    public Chalk getChalk(String text) {
        return Biome.printText(biome, text);
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public void setEnemies(Enemy[] enemies) {
        this.enemies = enemies;
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

    public void setVisibility(boolean visible) {
        isVisible = visible;
    }

    public void setEnemiesVisibility(boolean visible) {
        enemiesAreVisible = visible;
    }

    public static Location fromResultSet(ResultSet set) throws SQLException {
        int x = set.getInt("x");
        int y = set.getInt("y");
        String stringBiome = set.getString("biome");
        Biome parsedBiome = null;
        for (Biome biome : Biome.values()) {
            if (biome.toString().equals(stringBiome)) {
                parsedBiome = biome;
                break;
            }
        }
        if (parsedBiome == null) {
            return null;
        }
        boolean enemiesAreVisible = set.getBoolean("enemies_visible");
        boolean isVisible = set.getBoolean("visible");
        return new Location(x, y, parsedBiome, null, enemiesAreVisible, isVisible);
    }
}
