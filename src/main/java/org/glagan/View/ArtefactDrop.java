package org.glagan.View;

import org.glagan.Artefact.Artefact;
import org.glagan.Character.Hero;

public class ArtefactDrop extends View {
    protected Hero hero;
    protected Artefact artefact;

    public ArtefactDrop(Hero hero, Artefact artefact) {
        this.hero = hero;
        this.artefact = artefact;
    }

    @Override
    public void gui() {
        // TODO Auto-generated method stub
    }

    @Override
    public void console() {
        System.out.println();
        System.out.println("Your last opponent dropped an artefact on his death:");
        printArtefact(artefact);
        System.out.println("Do you want to equip it ?");
        Artefact equippedArtefact = hero.getArtefactInSlot(artefact.getSlot());
        if (equippedArtefact != null) {
            System.out.println("You currently have this artefact equipped:");
            printArtefact(equippedArtefact);
        } else {
            System.out.println("You currently do not have anything equipped in this slot");
        }
    }
}
