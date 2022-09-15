package org.glagan.World;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Map {
    @NotNull
    protected String name;

    @Min(1)
    protected int level;

    @Min(5)
    protected int size;

    @NotNull
    protected Location[][] locations;

    public Map(String name, int level, int size, Location[][] locations) {
        this.name = name;
        this.level = level;
        this.size = size;
        this.locations = locations;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getSize() {
        return size;
    }

    public Location[][] getLocations() {
        return locations;
    }

    public void setPositionVisible(int x, int y) {
        locations[x][y].setVisibility(true);
    }

    public void setEnemiesVisible(int x, int y) {
        locations[x][y].setEnemiesVisibility(true);
    }
}
