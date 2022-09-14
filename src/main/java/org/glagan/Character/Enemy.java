package org.glagan.Character;

import org.glagan.World.Caracteristics;

import jakarta.validation.constraints.NotNull;

public class Enemy {
    @NotNull
    protected String name;

    protected EnemyRank type;

    protected int level;

    protected Caracteristics caracteristics;
}
