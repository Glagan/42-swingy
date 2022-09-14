package org.glagan.Character;

import org.glagan.Core.Caracteristics;

public class Magician extends Hero {
    public Magician(String name) {
        super(name);
    }

    public String className() {
        return "Magician";
    }

    public Caracteristics getCaracteristicsPerLevel() {
        return new Caracteristics(5, 2, 30);
    }

    public Caracteristics getBaseCaracteristics() {
        return new Caracteristics(5, 2, 30);
    }
}
