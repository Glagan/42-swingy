package org.glagan.Character;

import org.glagan.Core.Caracteristics;

public class Paladin extends Hero {
    public Paladin(String name) {
        super(name);
    }

    public String className() {
        return "Paladin";
    }

    public Caracteristics getCaracteristicsPerLevel() {
        return new Caracteristics(2, 4, 40);
    }

    public Caracteristics getBaseCaracteristics() {
        return new Caracteristics(1, 4, 50);
    }

}
