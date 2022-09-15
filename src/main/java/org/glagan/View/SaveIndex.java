package org.glagan.View;

import org.glagan.Character.Hero;
import org.glagan.Core.Save;

public class SaveIndex extends View {
    protected Save[] saves;

    public SaveIndex(Save[] saves) {
        this.saves = saves;
    }

    @Override
    public void gui() {
    }

    @Override
    public void console() {
        System.out.println("\nSelect your hero");
        if (saves != null) {
            if (saves.length == 0) {
                System.out.println("You have no heroes yet !");
            }
            for (int i = 0; i < saves.length; i++) {
                Save save = saves[i];
                System.out.print(i + 1 + "\t");
                if (save.isCorrupted()) {
                    System.out.print(save.getPath() + ", Corrupted: " + save.getError());
                } else {
                    Hero hero = save.getGame().getHero();
                    System.out.print(
                            hero.getName() + ", " + hero.getClass().getSimpleName() + " level " + hero.getLevel());
                }
                System.out.println();
            }
        }
    }
}
