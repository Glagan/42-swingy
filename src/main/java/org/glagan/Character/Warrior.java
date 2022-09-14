package org.glagan.Character;

import org.glagan.World.Caracteristics;

public class Warrior extends Hero {
    public Warrior(String name) {
        super(name);
    }

    public String className() {
        return "Warrior";
    }

    public Caracteristics perLevel() {
        return new Caracteristics(3, 3, 4);
    }

    public Caracteristics baseCaracteristics() {
        return new Caracteristics(3, 4, 3);
    }
}
