package org.glagan.View;

import org.glagan.Character.Enemy;
import org.glagan.Character.Hero;
import org.glagan.Core.Caracteristics;

import org.glagan.Controller.Controller;

public class Encounter extends View {
    protected Hero hero;
    protected Enemy enemy;

    public Encounter(Controller controller, Hero hero, Enemy enemy) {
        super(controller);
        this.hero = hero;
        this.enemy = enemy;
    }

    @Override
    public void gui() {
    }

    @Override
    public void console() {
        System.out.println();
        System.out.println("You encountered an enemy !");
        System.out.println(enemy.getName() + ", level " + enemy.getLevel());
        Caracteristics caracteristics = enemy.getCaracteristics();
        System.out.println(caracteristics.getAttack() + " attack, " + caracteristics.getDefense() + " defense, "
                + caracteristics.getHitPoints() + " hp");
        System.out.println("(You) " + hero.getName() + ", level " + hero.getLevel());
        Caracteristics heroCaracteristics = hero.getFinalCaracteristics();
        System.out.println(heroCaracteristics.getAttack() + " attack, " + heroCaracteristics.getDefense() + " defense, "
                + heroCaracteristics.getHitPoints() + " hp");
    }
}
