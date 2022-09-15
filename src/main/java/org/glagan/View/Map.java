package org.glagan.View;

import org.glagan.Character.Hero;
import org.glagan.World.Coordinates;
import org.glagan.World.Location;

import com.github.tomaslanger.chalk.Chalk;

public class Map extends View {
    protected org.glagan.World.Map map;
    protected Hero hero;

    public Map(org.glagan.World.Map map, Hero hero) {
        this.map = map;
        this.hero = hero;
    }

    @Override
    public void gui() {
        // TODO Auto-generated method stub

    }

    @Override
    public void console() {
        Location[][] locations = map.getLocations();
        Coordinates heroPosition = hero.getPosition();
        int size = map.getSize();

        System.out.println();
        System.out.print("┌");
        for (int i = 0; i < size; i++) {
            System.out.print("─");
        }
        System.out.println("┐");

        for (int x = 0; x < size; x++) {
            System.out.print("│");
            for (int y = 0; y < size; y++) {
                Boolean hasPlayer = x == heroPosition.getX() && y == heroPosition.getY();
                Location location = locations[x][y];
                if (location.isVisible()) {
                    String text = " ";
                    if (hasPlayer) {
                        text = "P";
                    } else if (location.isEnemiesAreVisible() && location.hasEnemies()) {
                        text = "•";
                    }
                    Chalk out = location.getChalk(text);
                    if (hasPlayer) {
                        out.bold();
                    }
                    System.out.print(out);
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("│");
        }

        System.out.print("└");
        for (int i = 0; i < size; i++) {
            System.out.print("─");
        }
        System.out.println("┘");
    }
}
