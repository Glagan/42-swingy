package org.glagan.View;

import org.glagan.Character.Hero;
import org.glagan.World.Map;
import org.glagan.Controller.Controller;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

import com.github.tomaslanger.chalk.Chalk;

import net.miginfocom.swing.MigLayout;

public class Inventory extends View implements ActionListener {
    protected Map map;
    protected Hero hero;

    public Inventory(Controller controller, Map map, Hero hero) {
        super(controller);
        this.map = map;
        this.hero = hero;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("show")) {
            dispatch("show");
        }
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new MigLayout("fillx"));

        panel.add(new JLabel(hero.getName()), "span, wrap");
        JPanel levelPanel = new JPanel(new MigLayout("fillx, insets 0"));
        levelPanel.add(new JLabel(hero.className() + " level "));
        JLabel heroLevel = new JLabel("" + hero.getLevel());
        heroLevel.setForeground(new ColorUIResource(66, 66, 220));
        levelPanel.add(heroLevel);
        levelPanel.add(new JLabel(" (" + hero.getExperience() + "/" + hero.nextLevelExperience() + ")"), "wrap");
        panel.add(levelPanel, "span, wrap");

        panel.add(new JLabel("Currently in " + map.getName() + " (level " + map.getLevel() + ") [x"
                + hero.getPosition().getX() + "; y" + hero.getPosition().getY() + "]"), "span, wrap, gapbottom 8px");

        JPanel itemsPanel = new JPanel(new MigLayout("fillx, insets 0"));

        if (hero.getHelm() != null) {
            JPanel artefactPanel = renderArtefactGui(hero.getHelm());
            itemsPanel.add(artefactPanel, "center, wmax 33%");
        } else {
            itemsPanel.add(new JLabel("No Helmet"), "center");
        }

        if (hero.getArmor() != null) {
            JPanel artefactPanel = renderArtefactGui(hero.getArmor());
            itemsPanel.add(artefactPanel, "center, wmax 33%");
        } else {
            itemsPanel.add(new JLabel("No Armor"), "center");
        }

        if (hero.getWeapon() != null) {
            JPanel artefactPanel = renderArtefactGui(hero.getWeapon());
            itemsPanel.add(artefactPanel, "center, wmax 33%");
        } else {
            itemsPanel.add(new JLabel("No Weapon"), "center");
        }

        panel.add(itemsPanel, "span, wrap, grow");

        JPanel actionPanel = new JPanel(new MigLayout("fillx, align center, insets 0"));
        JButton backButton = new JButton("Back");
        backButton.setActionCommand("show");
        backButton.addActionListener(this);
        backButton.requestFocusInWindow();
        actionPanel.add(backButton, "span, center");
        panel.add(actionPanel, "span, wrap, grow, center, gap 0");

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void console() {
        System.out.println();
        System.out.println(hero.getName() + ", " + hero.className() + " level " + hero.getLevel() + " ("
                + hero.getExperience() + "/" + hero.nextLevelExperience() + ")");
        System.out.println("Currently in " + map.getName() + " (level " + map.getLevel() + ") [x"
                + hero.getPosition().getX() + "; y" + hero.getPosition().getY() + "]");

        if (hero.getHelm() != null) {
            printArtefactConsole(hero.getHelm());
        } else {
            System.out.println("Helmet\t" + Chalk.on("None").bold());
        }

        if (hero.getArmor() != null) {
            printArtefactConsole(hero.getArmor());
        } else {
            System.out.println("Armor\t" + Chalk.on("None").bold());
        }

        if (hero.getWeapon() != null) {
            printArtefactConsole(hero.getWeapon());
        } else {
            System.out.println("Weapon\t" + Chalk.on("None").bold());
        }
        waitGoBack("Press enter to go back");
    }
}
