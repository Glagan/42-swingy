package org.glagan;

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
}
