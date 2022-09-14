package org.glagan.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.glagan.Character.Enemy;
import org.glagan.Character.EnemyRank;
import org.glagan.Core.Caracteristics;

import jakarta.validation.constraints.Min;

public class MapGenerator {
    protected static MapGenerator generator = new MapGenerator();

    protected String[] namePrefixes = {
            "Sacred", "Damned", "Lost", "Unknown", "Calm"
    };

    protected String[] mapCores = {
            "Plain", "Forest", "Mountain", "Desert", "Taiga", "Tundra", "Swamp", "Savannah", "Badland", "Beach",
            "Ocean", "Arena"
    };

    protected String[] mapSuffixes = {
            "of Thran", "of the Pirates"
    };

    public MapGenerator() {
    }

    protected String name() {
        Random rand = new Random();

        String prefix = null;
        if (rand.nextBoolean()) {
            prefix = namePrefixes[rand.nextInt(namePrefixes.length)];
        }

        String core = mapCores[rand.nextInt(mapCores.length)];

        String suffix = null;
        if (rand.nextBoolean()) {
            suffix = mapSuffixes[rand.nextInt(mapSuffixes.length)];
        }

        return (prefix != null ? prefix + " " : "") + core + (suffix != null ? " " + suffix : "");
    }

    static protected int biomeLength = Biome.values().length;

    protected Location location(int level, int x, int y, int enemyCount) {
        Random rand = new Random();
        Biome biome = Biome.values()[rand.nextInt(biomeLength)];
        Enemy[] enemies = enemyCount > 0 ? new Enemy[enemyCount] : null;
        for (int i = 0; i < enemyCount; i++) {
            enemies[i] = new Enemy("Enemy", EnemyRank.SOLDIER, 1, new Caracteristics(10, 10, 10));
        }
        return new Location(x, y, biome, enemies, false, false);
    }

    public Map generate(@Min(1) int level) {
        Random rand = new Random();
        String name = this.name();
        int size = (level - 1) * 5 + 10 - level;
        double enemyChance = Math.pow(level + 1, 0.9);
        int maxEnemyCount = Math.floorDiv(level, 10) + 1;
        int roundsSinceLastGeneration = 2;
        List<List<Location>> locations = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            List<Location> row = new ArrayList<>();
            locations.add(row);
            for (int y = 0; y < size; y++) {
                double locationEnemyChance = enemyChance + (roundsSinceLastGeneration * 10);
                boolean hasEnemy = (rand.nextDouble() * 100) <= locationEnemyChance;
                int enemyCount = 0;
                if (hasEnemy) {
                    enemyCount = rand.nextInt(maxEnemyCount + 1);
                    if (enemyCount < 1 && enemyChance >= 100) {
                        enemyCount = 1;
                    }
                }
                if (enemyCount > 0) {
                    roundsSinceLastGeneration = 0;
                } else {
                    roundsSinceLastGeneration++;
                }
                row.add(this.location(level, x, y, enemyCount));
            }
        }
        return new Map(name, level, size, locations);
    }

    public static MapGenerator getGenerator() {
        return generator;
    }
}
