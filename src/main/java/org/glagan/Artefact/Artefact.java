package org.glagan.Artefact;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public static Artefact fromResultSet(ResultSet set) throws SQLException {
        // Save values
        String name = set.getString("name");
        String strRarity = set.getString("rarity");
        int attack = set.getInt("attack");
        int defense = set.getInt("defense");
        int hitPoints = set.getInt("hitpoints");
        String strSlot = set.getString("slot");

        // Parse enums
        ArtefactSlot slot = null;
        for (ArtefactSlot expectedSlot : ArtefactSlot.values()) {
            if (expectedSlot.toString().equals(strSlot)) {
                slot = expectedSlot;
                break;
            }
        }
        Rarity rarity = null;
        for (Rarity expectedRarity : Rarity.values()) {
            if (expectedRarity.toString().equals(strRarity)) {
                rarity = expectedRarity;
                break;
            }
        }

        // Validate
        if (slot == null || rarity == null || name == null) {
            return null;
        }

        return new Artefact(name, rarity, new Caracteristics(attack, defense, hitPoints), slot);
    }
}
