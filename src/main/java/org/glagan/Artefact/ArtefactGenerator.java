package org.glagan.Artefact;

import java.util.Random;

import org.glagan.Core.Caracteristics;

import jakarta.validation.constraints.Min;

public class ArtefactGenerator {
    protected static Random rand = new Random();
    protected static ArtefactGenerator generator = new ArtefactGenerator();

    protected String[] namePrefixes = {
            "Sacred", "Haunted", "Doomed", "Broken", "Shining", "Perfect"
    };

    protected String[] helmetCores = {
            "Hat", "Helmet", "Cap"
    };

    protected String[] armorCores = {
            "Vest", "Armor", "Shirt",
    };

    protected String[] weaponCores = {
            "Axe", "Sword", "Spear", "Wand", "Staff", "Bow", "Hammer"
    };

    protected String[] suffixes = {
            "of The Ancients", "of the Pirates"
    };

    protected ArtefactGenerator() {
    }

    protected String name(ArtefactSlot slot) {
        String prefix = null;
        if (rand.nextBoolean()) {
            prefix = namePrefixes[rand.nextInt(namePrefixes.length)];
        }

        String core;
        if (slot.equals(ArtefactSlot.HELM)) {
            core = helmetCores[rand.nextInt(helmetCores.length)];
        } else if (slot.equals(ArtefactSlot.ARMOR)) {
            core = armorCores[rand.nextInt(armorCores.length)];
        } else {
            core = weaponCores[rand.nextInt(weaponCores.length)];
        }

        String suffix = null;
        if (rand.nextBoolean()) {
            suffix = suffixes[rand.nextInt(suffixes.length)];
        }

        return (prefix != null ? prefix + " " : "") + core + (suffix != null ? " " + suffix : "");
    }

    protected ArtefactSlot slot() {
        double roll = rand.nextDouble();
        if (roll >= 0.66) {
            return ArtefactSlot.HELM;
        } else if (roll >= 0.33) {
            return ArtefactSlot.ARMOR;
        }
        return ArtefactSlot.WEAPON;
    }

    protected Rarity rarity() {
        double roll = rand.nextDouble();
        if (roll >= 0.90) {
            return Rarity.LEGENDARY;
        } else if (roll >= 0.70) {
            return Rarity.RARE;
        }
        return Rarity.COMMON;
    }

    protected Caracteristics bonuses(int level, ArtefactSlot slot, Rarity rarity) {
        Caracteristics caracteristics = new Caracteristics(0, 0, 0);
        int rarityBonus = rarity.equals(Rarity.LEGENDARY) ? 12 : rarity.equals(Rarity.RARE) ? 6 : 0;
        int statsPool = (level - 1) * 4 + 9 + rarityBonus;
        if (slot.equals(ArtefactSlot.HELM)) {
            int hitPoints = rand.nextInt((int) (statsPool * 0.8));
            statsPool -= hitPoints;
            caracteristics.setHitPoints(Math.max(hitPoints * 10, 10) + ((level / 2) * 10));
            int defense = rand.nextInt((int) (statsPool * 0.5));
            statsPool -= defense;
            caracteristics.setDefense(Math.max(defense, 1));
            caracteristics.setAttack(statsPool);
        } else if (slot.equals(ArtefactSlot.ARMOR)) {
            int defense = rand.nextInt((int) (statsPool * 0.8));
            statsPool -= defense;
            caracteristics.setDefense(Math.max(defense, 1) + level);
            int hitPoints = rand.nextInt((int) (statsPool * 0.5));
            statsPool -= hitPoints;
            caracteristics.setHitPoints(Math.max(hitPoints, 1) * 10);
            caracteristics.setAttack(statsPool);
        } else {
            caracteristics.setAttack(caracteristics.getAttack() + level);
            int attack = rand.nextInt((int) (statsPool * 0.8));
            statsPool -= attack;
            caracteristics.setAttack(Math.max(attack, 1) + level);
            int defense = rand.nextInt((int) (statsPool * 0.5));
            statsPool -= defense;
            caracteristics.setDefense(Math.max(defense, 1));
            caracteristics.setHitPoints(statsPool * 10);
        }
        return caracteristics;
    }

    public Artefact generate(@Min(1) int level) {
        ArtefactSlot slot = this.slot();
        String name = this.name(slot);
        Rarity rarity = this.rarity();
        Caracteristics bonuses = this.bonuses(level, slot, rarity);
        return new Artefact(name, rarity, bonuses, slot);
    }

    public static ArtefactGenerator getGenerator() {
        return generator;
    }
}
