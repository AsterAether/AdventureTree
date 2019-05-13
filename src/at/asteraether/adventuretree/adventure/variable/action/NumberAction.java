package at.asteraether.adventuretree.adventure.variable.action;

import at.asteraether.adventuretree.adventure.variable.Variable;
import at.asteraether.adventuretree.adventure.variable.VariableManager;
import at.asteraether.adventuretree.adventure.variable.VariableType;
import at.asteraether.adventuretree.io.IOHandler;

public abstract class NumberAction implements OptionAction {
    private String varName;
    private float num;
    private String numVarName;

    public NumberAction(String varName, float num) {
        this.varName = varName;
        this.num = num;
    }

    public NumberAction(String varName, String numVarName) {
        this.varName = varName;
        this.numVarName = numVarName;
    }

    @Override
    public VariableType getPossibleType() {
        return VariableType.NUMBER;
    }

    @Override
    public void perform(IOHandler handler, VariableManager varManager) {
        Variable variable = varManager.getVariable(varName);
        typeCheck(variable);

        if (numVarName == null) {
            variable.setValue(String.valueOf(performNumber(variable.asNumber(), num)));
        } else {
            Variable second = varManager.getVariable(numVarName);
            typeCheck(second);
            variable.setValue(String.valueOf(performNumber(variable.asNumber(), second.asNumber())));
        }
    }

    protected abstract float performNumber(float var, float var2);

    @Override
    public ActionType getActionType() {
        return ActionType.NUMBER;
    }
}
