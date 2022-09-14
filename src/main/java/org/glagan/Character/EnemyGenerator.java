package org.glagan.Character;

import java.util.Random;

import org.glagan.Core.Caracteristics;

import jakarta.validation.constraints.Min;

public class EnemyGenerator {
    protected static Random rand = new Random();
    protected static EnemyGenerator generator = new EnemyGenerator();

    protected String[] namePrefixes = {
            "Crazy", "Mad"
    };

    protected String[] nameCores = {
            "Orc", "Rat", "Slime", "Elf", "Minotaur", "Skeleton"
    };

    protected String[] nameSuffixes = {
            "King", "Slayer", "Spirit"
    };

    public EnemyGenerator() {
    }

    protected String name() {
        Random rand = new Random();

        String prefix = null;
        if (rand.nextBoolean()) {
            prefix = namePrefixes[rand.nextInt(namePrefixes.length)];
        }

        String core = nameCores[rand.nextInt(nameCores.length)];

        String suffix = null;
        if (rand.nextBoolean()) {
            suffix = nameSuffixes[rand.nextInt(nameSuffixes.length)];
        }

        return (prefix != null ? prefix + " " : "") + core + (suffix != null ? " " + suffix : "");
    }

    static protected int rankLength = EnemyRank.values().length;

    public Enemy generate(@Min(1) int level) {
        String name = this.name();
        EnemyRank rank = EnemyRank.values()[rand.nextInt(rankLength)];
        int rankBonus = rank.equals(EnemyRank.BOSS) ? 6 : rank.equals(EnemyRank.LIEUTNANT) ? 3 : 0;
        int statsPool = (level - 1) * 10 + 9 + rankBonus;
        Caracteristics caracteristics = new Caracteristics(1, 1, 10);
        double statsVariance = rand.nextDouble();
        if (statsVariance < 0.33) {
            int attack = rand.nextInt((int) (statsPool * 0.8));
            statsPool -= attack;
            caracteristics.setAttack(Math.max(attack, 1));
            int defense = rand.nextInt((int) (statsPool * 0.5));
            statsPool -= defense;
            caracteristics.setDefense(Math.max(defense, 1));
            caracteristics.setHitPoints(statsPool * 10);
        } else if (statsVariance < 0.66) {
            int defense = rand.nextInt((int) (statsPool * 0.8));
            statsPool -= defense;
            caracteristics.setDefense(Math.max(defense, 1));
            int attack = rand.nextInt((int) (statsPool * 0.5));
            statsPool -= attack;
            caracteristics.setAttack(Math.max(attack, 1));
            caracteristics.setHitPoints(statsPool * 10);
        } else {
            int hitPoints = rand.nextInt((int) (statsPool * 0.8));
            statsPool -= hitPoints;
            caracteristics.setHitPoints(Math.max(hitPoints * 10, 10));
            int attack = rand.nextInt((int) (statsPool * 0.5));
            statsPool -= attack;
            caracteristics.setAttack(Math.max(attack, 1));
            caracteristics.setDefense(statsPool);
        }
        return new Enemy(name, rank, level, caracteristics);
    }

    public static EnemyGenerator getGenerator() {
        return generator;
    }
}
