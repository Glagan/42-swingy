package org.glagan.Map;

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
}
