package org.glagan.Character;

import org.glagan.World.Caracteristics;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Enemy {
    @NotNull
    protected String name;

    @NotNull
    protected EnemyRank type;

    @NotNull
    @Min(1)
    protected int level;

    @NotNull
    protected Caracteristics caracteristics;
}
