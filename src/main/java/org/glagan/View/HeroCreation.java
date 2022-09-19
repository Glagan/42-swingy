package org.glagan.View;

import org.glagan.Character.Hero;
import org.glagan.Character.Magician;
import org.glagan.Character.Paladin;
import org.glagan.Character.Warrior;
import org.glagan.Controller.Controller;
import org.glagan.Core.Caracteristics;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;

public class HeroCreation extends View implements ActionListener {
    // GUI Variables
    protected JButton createButton;
    protected JTextField heroTextField;
    protected boolean validName;
    protected boolean validClass;
    protected JLabel attackLabel;
    protected JLabel defenseLabel;
    protected JLabel hpLabel;

    public HeroCreation(Controller controller) {
        super(controller);
    }

    protected void updateClassLabels(Hero hero) {
        Caracteristics baseCaracteristics = hero.getBaseCaracteristics();
        Caracteristics perLevel = hero.getCaracteristicsPerLevel();
        attackLabel.setText("Attack: " + baseCaracteristics.getAttack() + " (+" + perLevel.getAttack() + ")");
        defenseLabel.setText("Defense: " + baseCaracteristics.getDefense() + " (+" + perLevel.getDefense() + ")");
        hpLabel.setText("HP: " + baseCaracteristics.getHitPoints() + " (+" + perLevel.getAttack() + ")");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("select-magician")) {
            dispatch("set-class magician");
            updateClassLabels(new Magician("Test"));
            validClass = true;
        } else if (action.equals("select-paladin")) {
            dispatch("set-class paladin");
            updateClassLabels(new Paladin("Test"));
            validClass = true;
        } else if (action.equals("select-warrior")) {
            dispatch("set-class warrior");
            updateClassLabels(new Warrior("Test"));
            validClass = true;
        } else if (action.equals("create") && validName && validClass) {
            dispatch("finalize-hero");
        }
        if (validName && validClass) {
            createButton.setEnabled(true);
        } else {
            createButton.setEnabled(false);
        }
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new MigLayout("fillx"));
        JLabel classLabel = new JLabel("Select your class");
        panel.add(classLabel, "span, grow");

        JRadioButton magicianButton = new JRadioButton("Magician");
        magicianButton.setMnemonic(KeyEvent.VK_M);
        magicianButton.setActionCommand("select-magician");
        magicianButton.setSelected(false);
        magicianButton.addActionListener(this);

        JRadioButton paladinButton = new JRadioButton("Paladin");
        paladinButton.setMnemonic(KeyEvent.VK_P);
        paladinButton.setActionCommand("select-paladin");
        magicianButton.setSelected(false);
        paladinButton.addActionListener(this);

        JRadioButton warriorButton = new JRadioButton("Warrior");
        warriorButton.setMnemonic(KeyEvent.VK_W);
        warriorButton.setActionCommand("select-warrior");
        warriorButton.setSelected(false);
        warriorButton.addActionListener(this);

        ButtonGroup group = new ButtonGroup();
        group.add(magicianButton);
        group.add(paladinButton);
        group.add(warriorButton);

        JPanel classGlobalPanel = new JPanel(new GridLayout(0, 2));
        JPanel classSelectorPanel = new JPanel(new GridLayout(0, 1));
        classSelectorPanel.add(magicianButton);
        classSelectorPanel.add(paladinButton);
        classSelectorPanel.add(warriorButton);
        classGlobalPanel.add(classSelectorPanel);
        JPanel statsPanel = new JPanel(new GridLayout(0, 1));
        attackLabel = new JLabel();
        statsPanel.add(attackLabel);
        defenseLabel = new JLabel();
        statsPanel.add(defenseLabel);
        hpLabel = new JLabel();
        statsPanel.add(hpLabel);
        classGlobalPanel.add(statsPanel);
        panel.add(classGlobalPanel, "span, grow");

        panel.add(new JLabel("Enter your hero name"), "span, grow, gap");
        JPanel heroNamePanel = new JPanel(new GridLayout(0, 1));
        JLabel nameError = new JLabel("Invalid hero name");
        nameError.setForeground(new ColorUIResource(200, 66, 66));
        nameError.setVisible(false);

        heroTextField = new JTextField(20);
        heroTextField.setEditable(true);
        heroTextField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                check();
            }

            public void removeUpdate(DocumentEvent e) {
                check();
            }

            public void insertUpdate(DocumentEvent e) {
                check();
            }

            public void check() {
                boolean valid = dispatch("set-name " + heroTextField.getText());
                if (!valid) {
                    validName = false;
                    nameError.setVisible(true);
                } else {
                    validName = true;
                    nameError.setVisible(false);
                }
                if (validName && validClass) {
                    createButton.setEnabled(true);
                } else {
                    createButton.setEnabled(false);
                }
            }
        });
        heroNamePanel.add(heroTextField, "span");
        heroNamePanel.add(nameError, "span, grow, hidemode 1");
        panel.add(heroNamePanel, "span, grow");

        createButton = new JButton("Create");
        createButton.setEnabled(false);
        createButton.setActionCommand("create");
        createButton.addActionListener(this);
        panel.add(createButton, "span, center");

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void console() {
        System.out.println("\nWelcome to hero creation menu !");
        System.out.println("Only 2 details are needed to create your hero, it's name and a class.");
        System.out.println(
                "The only differences between classes is their appearance, the characteristics they have at the beginning and their gain per level.");
        System.out.println("Warrior:    3 (+3) Attack | 3 (+4) Defense | 4 (+3) Hitpoints");
        System.out.println("Magician:   5 (+6) Attack | 2 (+1) Defense | 3 (+3) Hitpoints");
        System.out.println("Paladin:    1 (+2) Attack | 4 (+4) Defense | 5 (+4) Hitpoints");
        waitInputAndLoopDispatch("> Name", "= ", "set-name");
        waitInputAndLoopDispatch("> Class (Magician, Paladin, Warrior)", "= ", "set-class");
        dispatch("finalize-hero");
    }
}
