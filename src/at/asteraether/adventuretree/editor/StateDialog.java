package at.asteraether.adventuretree.editor;

import at.asteraether.adventuretree.adventure.Adventure;
import at.asteraether.adventuretree.adventure.TextSpeed;
import at.asteraether.adventuretree.adventure.state.Option;
import at.asteraether.adventuretree.adventure.state.State;
import at.asteraether.adventuretree.adventure.variable.Variable;
import at.asteraether.adventuretree.adventure.variable.VariableType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class StateDialog extends JDialog {

    private OptionListModel model;
    private State state;
    private boolean accepted;
    private State nextState;

    private StateDialog(Window owner, State state) {
        super(owner, "StateDialog");
        this.state = state;
        nextState = state.getNext();
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 700);
        initComponents();
        setLocationRelativeTo(owner);
    }

    public static State openStateDialog(Window owner, State state) {
        StateDialog dia = new StateDialog(owner, state);
        dia.setVisible(true);
        if (dia.accepted) {
            return dia.state;
        } else {
            return state;
        }
    }

    private void initComponents() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea(state.getText());
        textArea.setBorder(new TitledBorder(new LineBorder(Color.black), "Text"));
        textArea.setPreferredSize(new Dimension(0, 150));
        JComboBox<TextSpeed> comboBox = new JComboBox<>(new Vector<>(Arrays.asList(TextSpeed.values())));
        comboBox.setBorder(new TitledBorder(new LineBorder(Color.black), "Textspeed"));
        comboBox.setSelectedItem(state.getTextSpeed());
        panel.add(textArea, BorderLayout.CENTER);

        JPanel existing = new JPanel(new GridLayout(1, 2));
        JComboBox<State> existingCombo = new JComboBox<>(new Vector<>(AdventureEditor.STATES));
        JCheckBox existingCheck = new JCheckBox("Existing state?");
        existing.add(existingCheck);
        existing.add(existingCombo);

        JPanel underPanel = new JPanel(new GridLayout(3, 1));
        underPanel.add(comboBox);

        underPanel.add(existing);

        JPanel nextPanel = new JPanel(new GridLayout());

        nextPanel.setBorder(new TitledBorder(new LineBorder(Color.black), "Next state"));

        JCheckBox checkBox = new JCheckBox("Next state enabled");
        checkBox.setSelected(state.getNext() != null);

        StateDialog outer = this;

        JButton nextStateButton = new JButton("Next state");
        nextStateButton.addActionListener(e -> {
            if (checkBox.isSelected()) {
                if (nextState == null) {
                    nextState = new State("");
                }
                State old = nextState;
                nextState = StateDialog.openStateDialog(outer, nextState);
                AdventureEditor.STATES.remove(old);
                AdventureEditor.STATES.add(nextState);
            }
        });


        nextPanel.add(checkBox, WEST);
        nextPanel.add(nextStateButton, CENTER);

        underPanel.add(nextPanel);

        panel.add(underPanel, BorderLayout.SOUTH);

        JList<Option> optionList = new JList<>(model = new OptionListModel(state.getOptions()));

        optionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && optionList.getSelectedValue() != null) {
                    Option option = OptionDialog.openOptionDialog(outer, optionList.getSelectedValue());
                    model.set(optionList.getSelectedIndex(), option);
                }
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add option");
        addItem.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Input the title of the option");
            model.add(new Option(name));
        });

        JMenuItem deleteItem = new JMenuItem("Delete option");
        popupMenu.add(addItem);
        popupMenu.add(deleteItem);

        optionList.setComponentPopupMenu(popupMenu);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            accepted = true;
            state.setTextSpeed((TextSpeed) comboBox.getSelectedItem());
            state.setText(textArea.getText());
            state.setOptions(model.optionList);
            if (checkBox.isSelected()) {
                state.setNext(nextState);
            } else if(existingCheck.isSelected()) {
                state.setNext((State) existingCombo.getSelectedItem());
            } else {
                state.setNext(null);
            }
            dispose();
        });
        buttonPanel.add(cancel);
        buttonPanel.add(save);

        c.add(panel, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(optionList);
        scroll.setBorder(new TitledBorder(new LineBorder(Color.black), "Options"));
        c.add(scroll, BorderLayout.CENTER);
        c.add(buttonPanel, BorderLayout.SOUTH);
    }

    private class OptionListModel extends AbstractListModel<Option> {

        private List<Option> optionList = new ArrayList<>();

        public OptionListModel(Option[] optionList) {
            this.optionList.addAll(Arrays.asList(optionList));
        }

        public void add(Option option) {
            optionList.add(option);
            fireContentsChanged(this, 0, this.getSize());
        }

        public void set(int index, Option option) {
            optionList.set(index, option);
            fireContentsChanged(this, 0, this.getSize());
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
