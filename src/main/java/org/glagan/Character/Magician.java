package org.glagan.Character;

import org.glagan.Map.Caracteristics;

public class Magician extends Hero {
    public Magician(String name) {
        super(name);
    }

    public String className() {
        return "Magician";
    }

    public Caracteristics perLevel() {
        return new Caracteristics(5, 2, 3);
    }

    public Caracteristics baseCaracteristics() {
        return new Caracteristics(5, 2, 3);
    }
}
