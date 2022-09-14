package org.glagan.World;

import java.util.List;

import org.glagan.Character.Enemy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Location {
    @NotNull
    @Min(0)
    protected int x;

    @NotNull
    @Min(0)
    protected int y;

    @NotNull
    protected Biome biome;

    protected List<Enemy> enemies;

    @NotNull
    protected boolean enemiesAreVisible;

    @NotNull
    protected boolean isVisible;
}
