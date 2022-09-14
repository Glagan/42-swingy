package org.glagan.World;

import java.util.List;

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
    protected List<List<Location>> locations;

    public Map(String name, int level, int size, List<List<Location>> locations) {
        this.name = name;
        this.level = level;
        this.size = size;
        this.locations = locations;
    }
}
