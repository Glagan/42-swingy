package org.glagan.Core;

import java.util.List;

public class FightReport {
    protected List<String> logs;
    protected FightCharacter winner;
    protected int experience;
    protected boolean _leveledUp;
    protected boolean _hasDrop;

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

    public boolean leveledUp() {
        return _leveledUp;
    }

    public boolean hasDrop() {
        return _hasDrop;
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
        this._leveledUp = leveledUp;
    }

    public void setHasDrop(boolean hasDrop) {
        this._hasDrop = hasDrop;
    }
}
