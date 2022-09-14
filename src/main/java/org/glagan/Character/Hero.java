package org.glagan.Character;

import org.glagan.Artefact.Artefact;
import org.glagan.Artefact.ArtefactSlot;
import org.glagan.Core.Caracteristics;
import org.glagan.World.Coordinates;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public abstract class Hero {
    @NotNull
    protected String name;

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

    protected Coordinates position;

    protected Hero(String name) {
        this.name = name;
        this.level = 1;
        this.experience = 0;
    }

    abstract public String className();

    abstract public Caracteristics perLevel();

    abstract public Caracteristics baseCaracteristics();

    public boolean equipArtefact(Artefact artefact, ArtefactSlot slot) {
        if (artefact.getSlot().equals(slot)) {
            switch (slot) {
                case HELM:
                    this.helm = artefact;
                    break;
                case ARMOR:
                    this.armor = artefact;
                    break;
                case WEAPON:
                    this.weapon = artefact;
                    break;
            }
            return true;
        }
        return false;
    }

    public void removeArtefact(ArtefactSlot slot) {
        switch (slot) {
            case HELM:
                this.helm = null;
                break;
            case ARMOR:
                this.armor = null;
                break;
            case WEAPON:
                this.weapon = null;
                break;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public long getExperience() {
        return experience;
    }

    public JsonElement serialize() {
        Gson gson = new Gson();
        return gson.toJsonTree(this);
    }
}
