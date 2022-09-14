package org.glagan.World;

public class Caracteristics {
    protected int attack;

    protected int defense;

    protected int hitPoints;

    public Caracteristics(int attack, int defense, int hitPoints) {
        this.attack = attack;
        this.defense = defense;
        this.hitPoints = hitPoints;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    /**
     * Add the caracteristics of the given Caracteristics to *this* instance
     * 
     * @param other Caracteristics
     */
    public void add(Caracteristics other) {
        this.attack += other.attack;
        this.defense += other.defense;
        this.hitPoints += other.hitPoints;
    }

    public Caracteristics clone() {
        return new Caracteristics(this.attack, this.defense, this.hitPoints);
    }
}
