package at.asteraether.adventuretree.adventure.variable.action;

import at.asteraether.adventuretree.adventure.variable.Variable;
import at.asteraether.adventuretree.adventure.variable.VariableManager;
import at.asteraether.adventuretree.adventure.variable.VariableType;
import at.asteraether.adventuretree.exceptions.IllegalActionException;
import at.asteraether.adventuretree.io.IOHandler;

import java.io.Serializable;

public interface OptionAction extends Serializable {

    VariableType getPossibleType();

    void perform(IOHandler handler, VariableManager varManager);

    default boolean typeCheck(Variable variable) throws IllegalActionException {
        if (getPossibleType() == null) {
            return true;
        }

        if (!variable.getType().equals(getPossibleType())) {
            throw new IllegalActionException(variable.getName(), variable.getType(), getPossibleType());
        }
        return true;
    }

    default ActionType getActionType() {
        return ActionType.OTHER;
    }
}
