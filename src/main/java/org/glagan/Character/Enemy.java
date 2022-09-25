package org.glagan.Character;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public String getName() {
        return name;
    }

    public EnemyRank getRank() {
        return rank;
    }

    public int getLevel() {
        return level;
    }

    public Caracteristics getCaracteristics() {
        return caracteristics;
    }

    public static Enemy fromResultSet(ResultSet set) throws SQLException {
        // Save values
        String name = set.getString("name");
        String strRank = set.getString("rank");
        int level = set.getInt("level");
        int attack = set.getInt("attack");
        int defense = set.getInt("defense");
        int hitPoints = set.getInt("hitpoints");

        // Parse enums
        EnemyRank rank = null;
        for (EnemyRank expectedRank : EnemyRank.values()) {
            if (expectedRank.toString().equals(strRank)) {
                rank = expectedRank;
                break;
            }
        }

        // Validate
        if (rank == null || name == null || level < 1 || attack < 1 || defense < 1 || defense < 1) {
            return null;
        }

        return new Enemy(name, rank, level, new Caracteristics(attack, defense, hitPoints));
    }
}
