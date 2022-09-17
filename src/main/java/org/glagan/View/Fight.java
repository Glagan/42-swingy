package org.glagan.View;

import org.glagan.Core.FightReport;

public class Fight extends View {
    protected FightReport report;

    public Fight(FightReport report) {
        this.report = report;
    }

    @Override
    public void gui() {
        // TODO Auto-generated method stub

    }

    @Override
    public void console() {
        System.out.println();
        System.out.println("Fight logs:");
        for (String log : report.getLogs()) {
            System.out.println("  " + log);
        }
    }
}
