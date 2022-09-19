package org.glagan.World;

import org.glagan.Character.Hero;

public class SubMap {
    static public int xSize = 11;
    static public int ySize = 15;

    protected Map original;
    protected Location[][] locations;
    protected Coordinates start;
    protected Coordinates offset;
    protected int size;

    public SubMap(Map original, Coordinates start, Coordinates offset) {
        this.original = original;
        this.start = start;
        this.offset = offset;
        this.size = original.getSize();
        this.locations = new Location[xSize][ySize];
        Location[][] originalLocations = original.getLocations();
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                int xReal = realX(x);
                int yReal = realY(y);
                if (xReal < 0 || yReal < 0 || xReal >= original.getSize() || yReal >= original.getSize()) {
                    this.locations[x][y] = null;
                } else {
                    this.locations[x][y] = originalLocations[xReal][yReal];
                }
            }
        }
    }

    public int realX(int x) {
        return start.getX() - offset.getX() + x;
    }

    public int realY(int y) {
        return start.getY() - offset.getY() + y;
    }

    public int getSize() {
        return size;
    }

    public Location[][] getLocations() {
        return locations;
    }

    static public SubMap fromHeroPosition(Map map, Hero hero) {
        return new SubMap(map, hero.getPosition(), new Coordinates(xSize / 2, ySize / 2));
    }
}
