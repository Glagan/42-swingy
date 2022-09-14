package org.glagan.Artefact;

import org.glagan.Core.Caracteristics;

import jakarta.validation.constraints.NotNull;

public class Artefact {
    @NotNull
    protected String name;

    @NotNull
    protected Rarity rarity;

    @NotNull
    protected Caracteristics bonuses;

    @NotNull
    protected ArtefactSlot slot;

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
