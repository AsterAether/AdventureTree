package at.asteraether.adventuretree.adventure.variable.action;

import at.asteraether.adventuretree.adventure.variable.Variable;
import at.asteraether.adventuretree.adventure.variable.VariableManager;
import at.asteraether.adventuretree.adventure.variable.VariableType;
import at.asteraether.adventuretree.io.IOHandler;

public class SetAction implements OptionAction {

    private String varName;
    private String value;

    public SetAction(String varName, String value) {
        this.varName = varName;
        this.value = value;
    }

    @Override
    public VariableType getPossibleType() {
        return null;
    }

    @Override
    public void perform(IOHandler handler, VariableManager variableManager) {
        Variable var = variableManager.getVariable(varName);
        var.setValue(value);
    }

    @Override
    public ActionType getActionType() {
        return ActionType.SET;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SetAction{" +
                "varName='" + varName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
