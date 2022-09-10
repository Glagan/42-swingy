package org.glagan.Character;

public class HeroFactory {
    public static Hero newHero(String type, String name) throws Exception {
        if (type.equals("Warrior")) {
            return new Warrior(name);
        } else if (type.equals("Magician")) {
            return new Magician(name);
        } else if (type.equals("Paladin")) {
            return new Paladin(name);
        }
        throw new Exception("Inexisting hero type " + type);
    }
}
