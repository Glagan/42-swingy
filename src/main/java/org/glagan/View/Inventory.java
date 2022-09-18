package org.glagan.View;

import org.glagan.Character.Hero;
import org.glagan.World.Map;

import com.github.tomaslanger.chalk.Chalk;

public class Inventory extends View {
    protected Map map;
    protected Hero hero;

    public Inventory(Map map, Hero hero) {
        this.map = map;
        this.hero = hero;
    }

    @Override
    public void gui() {
        // TODO Auto-generated method stub
    }

    @Override
    public void console() {
        System.out.println();
        System.out.println(hero.getName() + ", " + hero.className() + " level " + hero.getLevel() + " ("
                + hero.getExperience() + "/" + hero.nextLevelExperience() + ")");
        System.out.println("Currently in " + map.getName() + " (level " + map.getLevel() + ") [x"
                + hero.getPosition().getX() + "; y" + hero.getPosition().getY() + "]");

        if (hero.getHelm() != null) {
            printArtefact(hero.getHelm());
        } else {
            System.out.println("Helmet\t" + Chalk.on("None").bold());
        }

        if (hero.getArmor() != null) {
            printArtefact(hero.getArmor());
        } else {
            System.out.println("Armor\t" + Chalk.on("None").bold());
        }

        if (hero.getWeapon() != null) {
            printArtefact(hero.getWeapon());
        } else {
            System.out.println("Weapon\t" + Chalk.on("None").bold());
        }
    }
}
