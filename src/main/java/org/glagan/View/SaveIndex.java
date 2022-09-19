package org.glagan.View;

import org.glagan.Controller.Controller;
import org.glagan.Core.Save;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;

public class SaveIndex extends View implements ListSelectionListener, ActionListener {
    protected Save[] saves;

    // GUI Variables
    protected JPanel panel;
    protected DefaultListModel<Save> model;
    protected JList<Save> list;
    protected JButton playButton;
    protected JButton deleteButton;

    public SaveIndex(Controller controller, Save[] saves) {
        super(controller);
        this.saves = saves;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (list.getSelectedIndex() == -1) {
                playButton.setEnabled(false);
                deleteButton.setEnabled(false);
            } else {
                playButton.setEnabled(true);
                deleteButton.setEnabled(true);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals("play")) {
            if (list.getSelectedIndex() >= 0) {
                dispatch("select " + (list.getSelectedIndex() + 1));
            }
        } else if (action.equals("delete")) {
            if (list.getSelectedIndex() >= 0) {
                int index = list.getSelectedIndex();
                dispatch("delete " + (index + 1));
                model.remove(index);
                panel.repaint();
            }
        }
    }

    @Override
    public void gui() {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        if (saves.length > 0) {
            panel.add(new JLabel("Select your Hero"));
            model = new DefaultListModel<>();
            for (int i = 0; i < saves.length; i++) {
                Save save = saves[i];
                model.addElement(save);
            }
            list = new JList<>(model);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(this);
            JScrollPane listScrollPane = new JScrollPane(list);
            panel.add(listScrollPane);
        } else {
            JLabel noHeroes = new JLabel("You have no heroes yet !");
            noHeroes.setHorizontalAlignment(JLabel.CENTER);
            panel.add(noHeroes);
        }

        JPanel actionPanel = new JPanel();
        playButton = new JButton("Play");
        playButton.setEnabled(false);
        playButton.setActionCommand("play");
        playButton.addActionListener(this);
        actionPanel.add(playButton);
        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(this);
        actionPanel.add(deleteButton);
        panel.add(actionPanel);

        controller.getFrame().setContentPane(panel);
        controller.getUi().repaint();
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
                System.out.println(i + 1 + "\t" + save.toString());
            }
        }
        waitInputAndDispatch("> [s]elect {number} [c]reate [l]ist [d]elete {number}", null);
    }
}
