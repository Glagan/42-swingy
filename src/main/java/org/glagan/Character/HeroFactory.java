package org.glagan.Character;

public class HeroFactory {
    public static Class<?> classFromString(String type) {
        if (type.equalsIgnoreCase("Warrior")) {
            return Warrior.class;
        } else if (type.equalsIgnoreCase("Magician")) {
            return Magician.class;
        } else if (type.equalsIgnoreCase("Paladin")) {
            return Paladin.class;
        }
        return null;
    }

    public static Hero newHero(String type, String name) {
        if (type.equalsIgnoreCase("Warrior")) {
            return new Warrior(name);
        } else if (type.equalsIgnoreCase("Magician")) {
            return new Magician(name);
        } else if (type.equalsIgnoreCase("Paladin")) {
            return new Paladin(name);
        }
        return null;
    }
}
