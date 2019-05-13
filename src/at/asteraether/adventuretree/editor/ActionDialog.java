package at.asteraether.adventuretree.editor;

import at.asteraether.adventuretree.adventure.variable.action.ActionType;
import at.asteraether.adventuretree.adventure.variable.action.OptionAction;
import at.asteraether.adventuretree.adventure.variable.action.SetAction;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

import static java.awt.BorderLayout.SOUTH;

public class ActionDialog extends JDialog {

    private boolean accepted;
    private OptionAction action;

    public static OptionAction openOptionActionDialog(Window owner, OptionAction action) {
        ActionDialog dia = new ActionDialog(owner, action);
        dia.setVisible(true);
        if (dia.accepted) {
            return dia.action;
        } else {
            return action;
        }
    }

    private ActionDialog(Window owner, OptionAction action) {
        super(owner, "ActionDialog");
        this.action = action;

        if (this.action == null) {
            this.action = new SetAction("", "");
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

        JComboBox<ActionType> types = new JComboBox<>(new Vector<>(Arrays.asList(ActionType.values())));
        types.setSelectedItem(action.getActionType());

        c.add(types, BorderLayout.NORTH);

        JPanel center = new JPanel();

        center.removeAll();
        JTextField varName = new JTextField();
        JTextField varValue = new JTextField();

        center.setLayout(new GridLayout(2, 1));
        center.add(varName);
        center.add(varValue);

        c.add(center, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            accepted = true;
            SetAction setAction = (SetAction) action;
            setAction.setValue(varValue.getText());
            setAction.setVarName(varName.getText());
            dispose();
        });
        buttonPanel.add(cancel);
        buttonPanel.add(save);

        c.add(buttonPanel, SOUTH);
    }
}
