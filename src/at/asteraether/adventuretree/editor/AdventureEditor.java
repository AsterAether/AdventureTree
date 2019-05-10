package at.asteraether.adventuretree.editor;

import at.asteraether.adventuretree.adventure.Adventure;
import at.asteraether.adventuretree.adventure.TextSpeed;
import at.asteraether.adventuretree.adventure.state.State;
import at.asteraether.adventuretree.adventure.variable.Variable;
import at.asteraether.adventuretree.adventure.variable.VariableType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class AdventureEditor extends JFrame {

    private VariableListModel model;
    private State startState = new State("");

    private AdventureEditor() {
        super("AdventureEditor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);
        initComponents();
        setLocationRelativeTo(this);
    }

    private void initComponents() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        AdventureEditor outer = this;

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
            startState = StateDialog.openStateDialog(outer, startState);
        });
        button.setPreferredSize(new Dimension(0, 100));

        variableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        variableList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && variableList.getSelectedValue() != null) {
                    Variable var = VariableDialog.openVariableDialog(outer, variableList.getSelectedValue());
                    model.set(variableList.getSelectedIndex(), var);
                }
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem addItem = new JMenuItem("Add variable");
        addItem.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Input the name of the variable");
            if (name != null && !name.trim().equals("")) {
                Variable var = new Variable(name, VariableType.TEXT);
                model.add(var);
            }
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

        JTextField nameField = new JTextField();
        nameField.setBorder(new TitledBorder(new LineBorder(Color.black), "Name"));

        JPanel upper = new JPanel(new GridLayout(3, 1));

        JComboBox<TextSpeed> comboBox = new JComboBox<>(new Vector<>(Arrays.asList(TextSpeed.values())));
        upper.setPreferredSize(new Dimension(0, 200));

        upper.add(nameField);
        upper.add(button);
        upper.add(comboBox);
        panel.add(upper, BorderLayout.NORTH);
        JScrollPane pane = new JScrollPane(variableList);
        pane.setBorder(new TitledBorder(new LineBorder(Color.black), "Variables"));
        panel.add(pane, BorderLayout.CENTER);

        c.add(panel, BorderLayout.CENTER);

        setJMenuBar(topBar);

        saveItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setFileFilter(new FileNameExtensionFilter("Adventure Files", "adv"));
            if (chooser.showSaveDialog(outer) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();
                String path = f.getAbsolutePath();
                if (!path.endsWith(".adv")) {
                    path += ".adv";
                }

                Adventure adv = new Adventure(nameField.getText(), startState);
                adv.getIoHandler().setTextSpeed((TextSpeed) comboBox.getSelectedItem());
                for (Variable variable : model.variableList) {
                    adv.getVarManager().addVariable(variable.getName(), variable.getType());
                }

                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
                    oos.writeObject(adv);
                    oos.close();
                    adv.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        loadItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
            chooser.setFileFilter(new FileNameExtensionFilter("Adventure Files", "adv"));
            if (chooser.showOpenDialog(outer) == JFileChooser.APPROVE_OPTION) {
                File f = chooser.getSelectedFile();

                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                    Adventure adv = (Adventure) ois.readObject();
                    adv.close();
                    ois.close();

                    startState = adv.getStartState();
                    nameField.setText(adv.getName());
                    comboBox.setSelectedItem(adv.getIoHandler().getTextSpeed());
                    model.set(adv.getVarManager().getVariables());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
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

        public void set(int index, Variable variable) {
            variableList.set(index, variable);
            fireContentsChanged(this, 0, getSize());
        }

        public void set(Collection<Variable> collection) {
            variableList.clear();
            variableList.addAll(collection);
            fireContentsChanged(this, 0, getSize());
        }

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
