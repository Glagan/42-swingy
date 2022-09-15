package org.glagan.View;

import org.glagan.Character.Hero;
import org.glagan.World.Coordinates;
import org.glagan.World.Location;

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
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == heroPosition.getX() && y == heroPosition.getY()) {
                    System.out.print("P");
                } else if (locations[x][y].hasEnemies()) {
                    System.out.print("E");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
