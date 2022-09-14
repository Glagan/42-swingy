package org.glagan.Character;

public class HeroFactory {
    public static Class<?> classFromString(String type) {
        if (type.equals("Warrior")) {
            return Warrior.class;
        } else if (type.equals("Magician")) {
            return Magician.class;
        } else if (type.equals("Paladin")) {
            return Paladin.class;
        }
        return null;
    }

    public static Hero newHero(String type, String name) {
        if (type.equals("Warrior")) {
            return new Warrior(name);
        } else if (type.equals("Magician")) {
            return new Magician(name);
        } else if (type.equals("Paladin")) {
            return new Paladin(name);
        }
        return null;
    }
}
