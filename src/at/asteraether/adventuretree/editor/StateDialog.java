package at.asteraether.adventuretree.editor;

import at.asteraether.adventuretree.adventure.TextSpeed;
import at.asteraether.adventuretree.adventure.state.Option;
import at.asteraether.adventuretree.adventure.state.State;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class StateDialog extends JDialog {

    private OptionListModel model;
    private State state;

    public StateDialog(Window owner, State state) {
        super(owner, "StateDialog");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        initComponents();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(0, 150));
        JComboBox<TextSpeed> comboBox = new JComboBox<>(new Vector<>(Arrays.asList(TextSpeed.values())));
        panel.add(textArea, BorderLayout.CENTER);

        JPanel underPanel = new JPanel(new GridLayout(2, 1));
        underPanel.add(comboBox);

        JPanel nextPanel = new JPanel(new GridLayout());

        JCheckBox checkBox = new JCheckBox();
        JButton nextState = new JButton("Next state");
        nextState.addActionListener(e -> {
            if (checkBox.isSelected()) {
                new StateDialog(this, new State("test")).setVisible(true);
            }
        });


        nextPanel.add(checkBox, WEST);
        nextPanel.add(nextState, CENTER);

        underPanel.add(nextPanel);

        panel.add(underPanel, BorderLayout.SOUTH);

        JList<Option> optionList = new JList<>(model = new OptionListModel());

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add option");
        addItem.addActionListener(e -> {
            model.add(new Option("Test"));
        });

        JMenuItem deleteItem = new JMenuItem("Delete option");
        popupMenu.add(addItem);
        popupMenu.add(deleteItem);

        optionList.setComponentPopupMenu(popupMenu);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = new JButton("Save");
        buttonPanel.add(cancel);
        buttonPanel.add(save);

        c.add(panel, BorderLayout.NORTH);
        c.add(optionList, BorderLayout.CENTER);
        c.add(buttonPanel, BorderLayout.SOUTH);
    }

    private class OptionListModel extends AbstractListModel<Option> {

        private List<Option> optionList = new ArrayList<>();

        public void add(Option option) {
            fireContentsChanged(this, 0, this.getSize());
            optionList.add(option);
        }

        @Override
        public int getSize() {
            return optionList.size();
        }

        @Override
        public Option getElementAt(int index) {
            return optionList.get(index);
        }
    }
}
