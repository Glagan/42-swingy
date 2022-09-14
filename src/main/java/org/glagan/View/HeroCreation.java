package org.glagan.View;

public class HeroCreation extends View {
    @Override
    public void gui() {
        // TODO Auto-generated method stub
    }

    @Override
    public void console() {
        System.out.println("\nWelcome to hero creation menu !");
        System.out.println("Only 2 details are needed to create your hero, it's name and a class.");
        System.out.println(
                "The only differences between classes is their appearance, the characteristics they have at the beginning and their gain per level.");
        System.out.println("Warrior:    3 (+3) Attack | 3 (+4) Defense | 4 (+3) Hitpoints");
        System.out.println("Magician:   5 (+6) Attack | 2 (+1) Defense | 3 (+3) Hitpoints");
        System.out.println("Paladin:    1 (+2) Attack | 4 (+4) Defense | 5 (+4) Hitpoints");
    }
}
