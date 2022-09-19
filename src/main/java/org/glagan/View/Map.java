package org.glagan.View;

import org.glagan.Character.Hero;
import org.glagan.World.Coordinates;
import org.glagan.World.Location;
import org.glagan.World.SubMap;
import org.glagan.Controller.Controller;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;

import com.github.tomaslanger.chalk.Chalk;

import net.miginfocom.swing.MigLayout;

public class Map extends View implements ActionListener {
    protected org.glagan.World.Map map;
    protected Hero hero;

    // GUI Variables
    protected JLabel[][] locationLabels;

    public Map(Controller controller, org.glagan.World.Map map, Hero hero) {
        super(controller);
        this.map = map;
        this.hero = hero;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("move north")) {
            dispatch("move north");
            updateGUIMap();
        } else if (action.equals("move west")) {
            dispatch("move west");
            updateGUIMap();
        } else if (action.equals("move south")) {
            dispatch("move south");
            updateGUIMap();
        } else if (action.equals("move east")) {
            dispatch("move east");
            updateGUIMap();
        } else if (action.equals("inventory")) {
            dispatch("inventory");
        }
    }

    protected void updateGUIMap() {
        SubMap map = SubMap.fromHeroPosition(this.map, hero);
        Location[][] locations = map.getLocations();
        Coordinates heroPosition = hero.getPosition();

        for (int x = 0; x < SubMap.xSize; x++) {
            for (int y = 0; y < SubMap.ySize; y++) {
                JLabel square = locationLabels[x][y];
                Location location = locations[x][y];
                Boolean hasPlayer = map.realX(x) == heroPosition.getX() && map.realY(y) == heroPosition.getY();

                if (location == null) {
                    square.setText(" ");
                    square.setBackground(null);
                } else if (location.isVisible()) {
                    if (hasPlayer) {
                        square.setText("P");
                    } else if (location.isEnemiesAreVisible() && location.hasEnemies()) {
                        square.setText("•");
                    } else {
                        square.setText(" ");
                    }
                    square.setBackground(new ColorUIResource(66, 66, 200)); // TODO Set biome color
                } else {
                    square.setText(" ");
                    square.setBackground(new ColorUIResource(0, 0, 0));
                }
            }
        }
        controller.getUi().repaint();
    }

    @Override
    public void gui() {
        JPanel panel = new JPanel(new MigLayout("insets 0"));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel mapPanel = new JPanel(new GridLayout(11, 1));
        locationLabels = new JLabel[SubMap.xSize][SubMap.ySize];
        for (int x = 0; x < SubMap.xSize; x++) {
            JPanel row = new JPanel(new GridLayout(1, 15));
            for (int y = 0; y < SubMap.ySize; y++) {
                locationLabels[x][y] = new JLabel();
                locationLabels[x][y].setOpaque(true);
                locationLabels[x][y].setMinimumSize(new DimensionUIResource(32, 32));
                row.add(locationLabels[x][y]);
            }
            mapPanel.add(row);
        }
        updateGUIMap();
        panel.add(mapPanel, "wrap");

        JPanel actionsPanel = new JPanel(new MigLayout("fillx"));

        JPanel movePanel = new JPanel(new MigLayout("insets 0"));
        movePanel.add(Box.createGlue());
        JButton moveNorth = new JButton("North");
        moveNorth.setActionCommand("move north");
        moveNorth.addActionListener(this);
        movePanel.add(moveNorth);
        movePanel.add(Box.createGlue(), "wrap");
        JButton moveWest = new JButton("West");
        moveWest.setActionCommand("move west");
        moveWest.addActionListener(this);
        movePanel.add(moveWest);
        JButton moveSouth = new JButton("South");
        moveSouth.setActionCommand("move south");
        moveSouth.addActionListener(this);
        movePanel.add(moveSouth);
        JButton moveEast = new JButton("East");
        moveEast.setActionCommand("move east");
        moveEast.addActionListener(this);
        movePanel.add(moveEast);
        actionsPanel.add(movePanel, "width 50%");

        JPanel rightPanel = new JPanel(new MigLayout("insets 0, fillx, center"));
        JButton showInventory = new JButton("Inventory");
        showInventory.setActionCommand("inventory");
        showInventory.addActionListener(this);
        rightPanel.add(showInventory, "span, center");
        actionsPanel.add(rightPanel, "width 50%, grow, center");

        panel.add(actionsPanel);

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
    }

    @Override
    public void console() {
        SubMap map = SubMap.fromHeroPosition(this.map, hero);
        Location[][] locations = map.getLocations();
        Coordinates heroPosition = hero.getPosition();
        int size = map.getSize();

        System.out.println();
        System.out.print("┌");
        for (int i = 0; i < SubMap.ySize; i++) {
            if (heroPosition.getX() == 0 && heroPosition.getY() == i) {
                System.out.print("╨");
            } else {
                System.out.print("─");
            }
        }
        System.out.println("┐");

        for (int x = 0; x < SubMap.xSize; x++) {
            if (heroPosition.getY() == 0 && heroPosition.getX() == x) {
                System.out.print("╡");
            } else {
                System.out.print("│");
            }
            for (int y = 0; y < SubMap.ySize; y++) {
                Boolean hasPlayer = map.realX(x) == heroPosition.getX() && map.realY(y) == heroPosition.getY();
                Location location = locations[x][y];
                if (location == null) {
                    System.out.print(" ");
                } else if (location.isVisible()) {
                    String text = " ";
                    if (hasPlayer) {
                        text = "P";
                    } else if (location.isEnemiesAreVisible() && location.hasEnemies()) {
                        text = "•";
                    }
                    Chalk out = location.getChalk(text);
                    if (hasPlayer) {
                        out.bold();
                    }
                    System.out.print(out);
                } else {
                    System.out.print(" ");
                }
            }
            if (heroPosition.getY() == size - 1 && heroPosition.getX() == x) {
                System.out.println("╞");
            } else {
                System.out.println("│");
            }
        }

        System.out.print("└");
        for (int i = 0; i < SubMap.ySize; i++) {
            if (heroPosition.getX() == size - 1 && heroPosition.getY() == i) {
                System.out.print("╥");
            } else {
                System.out.print("─");
            }
        }
        System.out.println("┘");

        waitInputAndDispatch("> [s]how [i]nventory [m]ove {[n]orth|[e]ast|[s]outh|[w]est}", null);
    }
}
