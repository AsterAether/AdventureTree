package at.asteraether.adventuretree.editor;

import at.asteraether.adventuretree.adventure.state.Option;
import at.asteraether.adventuretree.adventure.variable.Variable;
import at.asteraether.adventuretree.adventure.variable.VariableType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class VariableDialog extends JDialog {

    private boolean accepted;
    private Variable var;

    private VariableDialog(Window owner, Variable var) {
        super(owner, "VariableDialog");
        this.var = var;
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 200);
        initComponents();
        setLocationRelativeTo(owner);
    }

    public static Variable openVariableDialog(Window owner, Variable var) {
        VariableDialog dia = new VariableDialog(owner, var);
        dia.setVisible(true);
        if (dia.accepted) {
            return dia.var;
        } else {
            return var;
        }
    }

    private void initComponents() {
        Container c = getContentPane();
        c.setLayout(new GridLayout(4, 1));

        JTextField nameField = new JTextField(var.getName());
        nameField.setBorder(new TitledBorder(new LineBorder(Color.black), "Name"));

        JComboBox<VariableType> typeBox = new JComboBox<>(new Vector<>(Arrays.asList(VariableType.values())));
        typeBox.setBorder(new TitledBorder(new LineBorder(Color.black), "Type"));

        JTextField valueField = new JTextField(var.getDefaultValue());
        valueField.setBorder(new TitledBorder(new LineBorder(Color.black), "Starting value"));

        c.add(nameField);
        c.add(typeBox);
        c.add(valueField);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> dispose());
        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            accepted = true;
            var.setName(nameField.getText());
            var.setDefaultValue(valueField.getText());
            var.setType((VariableType) typeBox.getSelectedItem());
            dispose();
        });
        buttonPanel.add(cancel);
        buttonPanel.add(save);

        c.add(buttonPanel);
    }
}
