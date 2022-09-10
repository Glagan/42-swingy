package org.glagan.Character;

import org.glagan.Map.Caracteristics;

public class Paladin extends Hero {
    public Paladin(String name) {
        super(name);
    }

    public String className() {
        return "Paladin";
    }

    public Caracteristics perLevel() {
        return new Caracteristics(2, 4, 4);
    }

    public Caracteristics baseCaracteristics() {
        return new Caracteristics(1, 4, 5);
    }

}