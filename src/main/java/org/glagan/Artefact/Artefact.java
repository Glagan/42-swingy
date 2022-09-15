package org.glagan.Artefact;

import org.glagan.Core.Caracteristics;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class Artefact {
    @NotNull
    protected String name;

    @NotNull
    protected Rarity rarity;

    @NotNull
    @Valid
    protected Caracteristics bonuses;

    @NotNull
    protected ArtefactSlot slot;

    public Artefact(@NotNull String name, @NotNull Rarity rarity, @NotNull @Valid Caracteristics bonuses,
            @NotNull ArtefactSlot slot) {
        this.name = name;
        this.rarity = rarity;
        this.bonuses = bonuses;
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Caracteristics getBonuses() {
        return bonuses;
    }

    public ArtefactSlot getSlot() {
        return slot;
    }
}
