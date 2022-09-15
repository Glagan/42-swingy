package org.glagan.Character;

import org.glagan.Artefact.Artefact;
import org.glagan.Artefact.ArtefactSlot;
import org.glagan.Core.Caracteristics;
import org.glagan.World.Coordinates;
import org.glagan.World.Direction;

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

    abstract public Caracteristics getCaracteristicsPerLevel();

    abstract public Caracteristics getBaseCaracteristics();

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

    public Artefact getWeapon() {
        return weapon;
    }

    public Artefact getArmor() {
        return armor;
    }

    public Artefact getHelm() {
        return helm;
    }

    public Caracteristics getFinalCaracteristics() {
        return finalCaracteristics;
    }

    public Coordinates getPosition() {
        return position;
    }

    public void setPosition(Coordinates coordinates) {
        position = coordinates;
    }

    public void move(Direction direction) {
        switch (direction) {
            case NORTH:
                position.addX(-1);
                break;
            case EAST:
                position.addY(1);
                break;
            case SOUTH:
                position.addX(1);
                break;
            case WEST:
                position.addY(-1);
                break;
        }
    }

    /**
     * Solve equation x = (y * 1000) + (y - 1)^2 * 450 for variable y (level) to
     * extract the level from the experience amount
     * https://www.wolframalpha.com/widgets/view.jsp?id=c778a2d8bf30ef1d3c2d6bc5696defad
     */
    public void initializeCaracteristics() {
        level = Math.max((int) Math.floor((1 / 90) * (Math.sqrt(18 * experience - 8000) - 10)), 1);
        baseCaracteristics = getBaseCaracteristics();
        Caracteristics perLevel = getCaracteristicsPerLevel();
        finalCaracteristics = new Caracteristics(
                baseCaracteristics.getAttack() + (perLevel.getAttack() * (level - 1)),
                baseCaracteristics.getDefense() + (perLevel.getDefense() * (level - 1)),
                baseCaracteristics.getHitPoints() + (perLevel.getHitPoints() * (level - 1)));
        if (weapon != null) {
            finalCaracteristics.add(weapon.getBonuses());
        }
        if (armor != null) {
            finalCaracteristics.add(armor.getBonuses());
        }
        if (helm != null) {
            finalCaracteristics.add(helm.getBonuses());
        }
    }

    public JsonElement serialize() {
        Gson gson = new Gson();
        return gson.toJsonTree(this);
    }
}
