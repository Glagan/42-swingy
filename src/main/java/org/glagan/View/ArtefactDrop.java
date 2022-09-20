package org.glagan.View;

import org.glagan.Artefact.Artefact;
import org.glagan.Character.Hero;
import org.glagan.Controller.Controller;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class ArtefactDrop extends View implements ActionListener {
    protected Hero hero;
    protected Artefact artefact;

    public ArtefactDrop(Controller controller, Hero hero, Artefact artefact) {
        super(controller);
        this.hero = hero;
        this.artefact = artefact;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("equip")) {
            dispatch("equip");
        } else if (action.equals("leave")) {
            dispatch("leave");
        }
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new MigLayout("fillx"));

        panel.add(new JLabel("Your last opponent dropped an artefact on his death"), "span, wrap");

        JPanel artefactPanel = renderArtefactGui(artefact);
        Artefact equippedArtefact = hero.getArtefactInSlot(artefact.getSlot());
        if (equippedArtefact != null) {
            JPanel equippedArtefactPanel = renderArtefactGui(equippedArtefact);
            equippedArtefactPanel.add(new JLabel("(Currently equipped)"), "span, wrap, center");
            JPanel comparisonPanel = new JPanel(new MigLayout("fillx, insets 0"));
            comparisonPanel.add(artefactPanel, "center, wmax 75%");
            comparisonPanel.add(equippedArtefactPanel, "center, wmax 75%");
            panel.add(comparisonPanel, "span, wrap, grow");
        } else {
            panel.add(artefactPanel, "span, wrap, grow, center, wmax 50%");
        }

        JPanel actionPanel = new JPanel(new MigLayout("insets 0, fillx, center"));
        JButton equipButton = new JButton("Equip");
        equipButton.setMnemonic(KeyEvent.VK_E);
        equipButton.setActionCommand("equip");
        equipButton.addActionListener(this);
        actionPanel.add(equipButton, "width 50%, center");
        JButton leaveButton = new JButton("Leave");
        leaveButton.setMnemonic(KeyEvent.VK_L);
        leaveButton.setActionCommand("leave");
        leaveButton.addActionListener(this);
        actionPanel.add(leaveButton, "width 50%, center");
        panel.add(actionPanel, "span, grow, center");

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void console() {
        System.out.println();
        System.out.println("Your last opponent dropped an artefact on his death:");
        printArtefactConsole(artefact);
        System.out.println("Do you want to equip it ?");
        Artefact equippedArtefact = hero.getArtefactInSlot(artefact.getSlot());
        if (equippedArtefact != null) {
            System.out.println("You currently have this artefact equipped:");
            printArtefactConsole(equippedArtefact);
        } else {
            System.out.println("You currently do not have anything equipped in this slot");
        }
        waitInputAndDispatch("> [e]quip [l]eave", null);
    }
}
