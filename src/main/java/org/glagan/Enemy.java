package org.glagan;

import jakarta.validation.constraints.NotNull;

public class Enemy {
    @NotNull
    protected String name;

    protected EnemyType type;

    protected int level;

    protected Caracteristics caracteristics;
}
