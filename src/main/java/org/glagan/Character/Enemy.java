package org.glagan.Character;

import org.glagan.Core.Caracteristics;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Enemy {
    @NotNull
    protected String name;

    @NotNull
    protected EnemyRank rank;

    @NotNull
    @Min(1)
    protected int level;

    @NotNull
    @Valid
    protected Caracteristics caracteristics;

    public Enemy(@NotNull String name, @NotNull EnemyRank rank, @NotNull @Min(1) int level,
            @NotNull Caracteristics caracteristics) {
        this.name = name;
        this.rank = rank;
        this.level = level;
        this.caracteristics = caracteristics;
    }
}
