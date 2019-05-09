package at.asteraether.adventuretree.adventure;

import at.asteraether.adventuretree.adventure.state.Option;
import at.asteraether.adventuretree.adventure.state.State;
import at.asteraether.adventuretree.adventure.variable.VariableManager;
import at.asteraether.adventuretree.adventure.variable.VariableType;
import at.asteraether.adventuretree.adventure.variable.action.OptionAction;
import at.asteraether.adventuretree.exceptions.NoEndLeafException;
import at.asteraether.adventuretree.exceptions.NoStartStateFoundException;
import at.asteraether.adventuretree.exceptions.OutputException;
import at.asteraether.adventuretree.exceptions.VariableNotFoundException;
import at.asteraether.adventuretree.io.IOHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class Adventure implements Serializable {

    private final String name;
    private State startState;
    private transient final IOHandler ioHandler;
    private final VariableManager varManager;


    public Adventure(String name, OutputStream outputStream, InputStream inputStream, State startState, TextSpeed textSpeed) {
        this.startState = startState;
        ioHandler = new IOHandler(outputStream, inputStream, textSpeed);
        varManager = new VariableManager();
        this.name = name;
    }

    public Adventure(String name, State startState, TextSpeed textSpeed) {
        this(name, System.out, System.in, startState, textSpeed);
    }

    public Adventure(String name, State startState) {
        this(name, startState, TextSpeed.NORMAL);
    }

    public Adventure(String name) {
        this(name, null, TextSpeed.NORMAL);
    }

    public Adventure(String name, TextSpeed textSpeed) {
        this(name, null, textSpeed);
    }

    public void setStartState(State startState) {
        this.startState = startState;
    }

    public void addVariable(String name, VariableType type) {
        varManager.addVariable(name, type);
    }

    public void addVariable(String name, VariableType type, String startValue) {
        varManager.addVariable(name, type, startValue);
    }

    public void play() throws NoEndLeafException, OutputException, NoStartStateFoundException {
        if (startState == null) {
            throw new NoStartStateFoundException();
        }

        final String header = name.toUpperCase() + "\n\n";

        State current = startState;

        while (current != null) {
            clearConsole();
            writeInstant(header);

            TextSpeed normal = ioHandler.getTextSpeed();
            if (current.getTextSpeed() != null && current.getTextSpeed() != TextSpeed.DEFAULT)
                ioHandler.setTextSpeed(current.getTextSpeed());
            writeNL(current.getText());
            ioHandler.setTextSpeed(normal);

            newLine();

            Option[] options = current.getOptions();
            if (options.length == 0 && current.getNext() != null) {
                ioHandler.waitForEnter();
                current = current.getNext();
                continue;
            } else if (options.length == 0) {
                boolean again = showPrompt(header + current.getText(), "Play again?\n1. Yes\n2. No\n", new Boolean[]{true, false});
                if (again) {
                    current = startState;
                    reset();
                    continue;
                }
                break;
            }

            StringBuilder display = new StringBuilder();
            for (int i = 0; i < options.length; i++) {
                display.append(i + 1)
                        .append(". ")
                        .append(options[i].getTitle())
                        .append('\n');
            }

            Option chosen = showPrompt(header + current.getText(), display.toString(), options);
            OptionAction[] actions = chosen.getActions();
            for (OptionAction ac :
                    actions) {
                ac.perform(ioHandler, varManager);
            }
            current = chosen.getNext();
        }
        if (current == null) {
            throw new NoEndLeafException();
        }
        clearConsole();

        try {
            ioHandler.close();
        } catch (IOException e) {
            throw new OutputException();
        }
    }

    public <T> T showPrompt(String header, String prompt, T[] options) throws OutputException, VariableNotFoundException {
        header = variablePass(header);
        prompt = variablePass(prompt);
        return ioHandler.showPrompt(header, prompt, options);
    }

    public void newLine() throws OutputException {
        ioHandler.newLine();
    }

    public void writeInstant(String text) throws OutputException, VariableNotFoundException {
        text = variablePass(text);
        ioHandler.writeInstant(text);
    }

    public void writeInstantNL(String text) throws OutputException, VariableNotFoundException {
        text = variablePass(text);
        ioHandler.writeInstantNL(text);
    }

    public void writeNL(String text) throws OutputException, VariableNotFoundException {
        text = variablePass(text);
        ioHandler.writeNL(text);
    }

    public void write(String text) throws OutputException, VariableNotFoundException {
        text = variablePass(text);
        ioHandler.write(text);
    }

    public void clearConsole() {
        ioHandler.clearConsole();
    }

    private void reset() {
        varManager.reset();
    }

    public String variablePass(String text) throws VariableNotFoundException {
        return varManager.variablePass(text);
    }
}
