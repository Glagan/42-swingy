package org.glagan.Core;

import java.util.List;

public class FightReport {
    protected List<String> logs;
    protected FightCharacter winner;

    public FightReport() {
    }

    public List<String> getLogs() {
        return logs;
    }

    public FightCharacter getWinner() {
        return winner;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public void setWinner(FightCharacter winner) {
        this.winner = winner;
    }
}
