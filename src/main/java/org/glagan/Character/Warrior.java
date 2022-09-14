package org.glagan.Character;

import org.glagan.Core.Caracteristics;

public class Warrior extends Hero {
    public Warrior(String name) {
        super(name);
    }

    public String className() {
        return "Warrior";
    }

    public Caracteristics getCaracteristicsPerLevel() {
        return new Caracteristics(3, 3, 40);
    }

    public Caracteristics getBaseCaracteristics() {
        return new Caracteristics(3, 4, 30);
    }
}
