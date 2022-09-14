package org.glagan.World;

import java.util.List;

import org.glagan.Character.Enemy;

public class Location {
    protected int x;

    protected int y;

    protected Biome biome;

    protected List<Enemy> enemies;

    protected boolean enemiesAreVisible;

    protected boolean isVisible;
}
