package org.glagan.View;

import org.glagan.Artefact.Artefact;
import org.glagan.Character.Hero;
import org.glagan.Core.Caracteristics;
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

    protected void printArtefact(Artefact artefact) {
        System.out.println(Chalk.on(artefact.getName()).underline());
        Caracteristics bonuses = artefact.getBonuses();
        System.out.print("\t");
        boolean wrote = false;
        if (bonuses.getAttack() != 0) {
            if (bonuses.getAttack() > 0) {
                System.out.print("+" + bonuses.getAttack());
            } else {
                System.out.print(bonuses.getAttack());
            }
            System.out.print(" attack");
            wrote = true;
        }
        if (bonuses.getDefense() != 0) {
            if (wrote) {
                System.out.print(", ");
            }
            if (bonuses.getDefense() > 0) {
                System.out.print("+" + bonuses.getDefense());
            } else {
                System.out.print(bonuses.getDefense());
            }
            System.out.print(" defense");
            wrote = true;
        }
        if (bonuses.getHitPoints() != 0) {
            if (wrote) {
                System.out.print(", ");
            }
            if (bonuses.getHitPoints() > 0) {
                System.out.print("+" + bonuses.getHitPoints());
            } else {
                System.out.print(bonuses.getHitPoints());
            }
            System.out.print(" hp");
            wrote = true;
        }
        System.out.println();
    }

    @Override
    public void console() {
        System.out.println();
        System.out.println(hero.getName() + ", " + hero.className() + " level " + hero.getLevel());
        System.out.println("Currently in " + map.getName() + " (level " + map.getLevel() + ") at x"
                + hero.getPosition().getX() + " y" + hero.getPosition().getY());

        System.out.print("Helmet\t");
        if (hero.getHelm() != null) {
            printArtefact(hero.getHelm());
        } else {
            System.out.println(Chalk.on("None").bold());
        }

        System.out.print("Armor\t");
        if (hero.getArmor() != null) {
            printArtefact(hero.getArmor());
        } else {
            System.out.println(Chalk.on("None").bold());
        }

        System.out.print("Weapon\t");
        if (hero.getWeapon() != null) {
            printArtefact(hero.getWeapon());
        } else {
            System.out.println(Chalk.on("None").bold());
        }
    }
}
