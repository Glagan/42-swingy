package org.glagan.World;

import jakarta.validation.Valid;
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
    @Valid
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

    public boolean validateLocations() {
        if (locations.length != size) {
            return false;
        }
        for (int x = 0; x < size; x++) {
            if (locations[x] == null || locations[x].length != size) {
                return false;
            }
            for (int y = 0; y < size; y++) {
                Location location = locations[x][y];
                if (location.getX() != x || location.getY() != y) {
                    return false;
                }
            }
        }
        return true;
    }

    public void removeEnemies(int x, int y) {
        locations[x][y].setEnemies(null);
    }

    public void setPositionVisible(int x, int y) {
        locations[x][y].setVisibility(true);
    }

    public void setEnemiesVisible(int x, int y) {
        locations[x][y].setEnemiesVisibility(true);
    }
}
