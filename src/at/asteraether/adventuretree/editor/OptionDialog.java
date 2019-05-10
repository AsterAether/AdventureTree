package at.asteraether.adventuretree.editor;

import at.asteraether.adventuretree.adventure.state.Option;
import at.asteraether.adventuretree.adventure.state.State;
import at.asteraether.adventuretree.adventure.variable.VariableType;
import at.asteraether.adventuretree.adventure.variable.action.OptionAction;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class OptionDialog extends JDialog {

    private ActionListModel model;
    private boolean accepted;
    private State nextState;
    private Option option;

    public static Option openOptionDialog(Window owner, Option option) {
        OptionDialog dia = new OptionDialog(owner, option);
        dia.setVisible(true);
        if (dia.accepted) {
            return dia.option;
        } else {
            return option;
        }
    }

    private OptionDialog(Window owner, Option option) {
        super(owner, "OptionDialog");
        this.option = option;
        this.nextState = option.getNext();
        if (this.nextState == null) {
            this.nextState = new State("");
        }
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 650);
        initComponents();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        JTextField nameField = new JTextField(option.getTitle());
        nameField.setBorder(new TitledBorder(new LineBorder(Color.black), "Title"));

        JButton nextStateButton = new JButton("Next state");

        OptionDialog outer = this;

        nextStateButton.addActionListener(e -> {
            nextState = StateDialog.openStateDialog(outer, nextState);
        });

        JPanel upper = new JPanel(new GridLayout(2, 1));
        upper.add(nameField);
        upper.add(nextStateButton);

        c.add(upper, BorderLayout.NORTH);

        JList<OptionAction> actionList = new JList<>(model = new ActionListModel());

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add action");
        JMenuItem deleteItem = new JMenuItem("Delete action");
        popupMenu.add(addItem);
        popupMenu.add(deleteItem);

        actionList.setComponentPopupMenu(popupMenu);


        JScrollPane scroll = new JScrollPane(actionList);
        scroll.setBorder(new TitledBorder(new LineBorder(Color.black), "Actions"));
        c.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            accepted = true;
            option.setTitle(nameField.getText());
            option.setNext(nextState);
            dispose();
        });
        buttonPanel.add(cancel);
        buttonPanel.add(save);

        c.add(buttonPanel, BorderLayout.SOUTH);
    }

    private class ActionListModel extends AbstractListModel<OptionAction> {

        private List<OptionAction> optionList = new ArrayList<>();

        public void add(OptionAction optionAction) {
            fireContentsChanged(this, 0, this.getSize());
            optionList.add(optionAction);
        }

        @Override
        public int getSize() {
            return optionList.size();
        }

        @Override
        public OptionAction getElementAt(int index) {
            return optionList.get(index);
        }
    }
}
