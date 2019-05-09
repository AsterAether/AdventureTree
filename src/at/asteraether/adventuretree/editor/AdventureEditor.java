package at.asteraether.adventuretree.editor;

import at.asteraether.adventuretree.adventure.Adventure;
import at.asteraether.adventuretree.adventure.state.State;
import at.asteraether.adventuretree.adventure.variable.Variable;
import at.asteraether.adventuretree.adventure.variable.VariableType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdventureEditor extends JFrame {

    private Adventure adventure;
    private VariableListModel model;

    public AdventureEditor() {
        super("AdventureEditor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        initComponents();
        setLocationRelativeTo(this);
    }

    private void initComponents() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        JMenuBar topBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadItem = new JMenuItem("Load adventure");
        JMenuItem saveItem = new JMenuItem("Save adventure");

        fileMenu.add(loadItem);
        fileMenu.add(saveItem);

        topBar.add(fileMenu);


        JList<Variable> variableList = new JList<>(model = new VariableListModel());

        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton("Start-State");
        button.addActionListener(e -> {
            new StateDialog(this, new State("test")).setVisible(true);
        });
        button.setPreferredSize(new Dimension(0, 100));

        variableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add variable");
        addItem.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Input the name of the variable");
            Variable var = new Variable(name, VariableType.TEXT);
            model.add(var);
        });

        JMenuItem deleteItem = new JMenuItem("Delete variable");
        deleteItem.addActionListener(e -> {
            Variable var = variableList.getSelectedValue();
            if (var != null) {
                model.delete(var);
            }
        });
        popupMenu.add(addItem);
        popupMenu.add(deleteItem);

        variableList.setComponentPopupMenu(popupMenu);

        panel.add(button, BorderLayout.NORTH);
        JScrollPane pane = new JScrollPane(variableList);
        pane.setBorder(new TitledBorder(new LineBorder(Color.black), "Variables"));
        panel.add(pane, BorderLayout.CENTER);

        c.add(panel, BorderLayout.CENTER);

        setJMenuBar(topBar);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new AdventureEditor().setVisible(true);
            } catch (Exception ignored) {
            }
        });
    }

    private class VariableListModel extends AbstractListModel<Variable> {

        private List<Variable> variableList = new ArrayList<>();

        public void add(Variable variable) {
            variableList.add(variable);
            fireContentsChanged(this, 0, getSize());
        }

        public void delete(Variable variable) {
            variableList.remove(variable);
            fireContentsChanged(this, 0, getSize());
        }

        @Override
        public int getSize() {
            return variableList.size();
        }

        @Override
        public Variable getElementAt(int index) {
            return variableList.get(index);
        }
    }

}
