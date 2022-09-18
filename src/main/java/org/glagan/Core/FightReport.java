package org.glagan.Core;

import java.util.List;

public class FightReport {
    protected List<String> logs;
    protected FightCharacter winner;
    protected int experience;
    protected boolean leveledUp;

    public FightReport() {
    }

    public List<String> getLogs() {
        return logs;
    }

    public FightCharacter getWinner() {
        return winner;
    }

    public int getExperience() {
        return experience;
    }

    public boolean getLeveledUp() {
        return leveledUp;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public void setWinner(FightCharacter winner) {
        this.winner = winner;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setLeveledUp(boolean leveledUp) {
        this.leveledUp = leveledUp;
    }
}
