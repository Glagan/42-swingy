package org.glagan;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Hero {
    @NotNull
    protected String name;

    @NotNull
    protected HeroClass heroClass;

    @Min(1)
    @Max(100)
    protected int level;

    @Min(0)
    protected long experience;

    @NotNull
    protected Caracteristics baseCaracteristics;

    @NotNull
    protected Caracteristics finalCaracteristics;

    protected Artefact weapon;

    protected Artefact armor;

    protected Artefact helm;
}
