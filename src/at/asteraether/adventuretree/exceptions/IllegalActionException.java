package at.asteraether.adventuretree.exceptions;

import at.asteraether.adventuretree.adventure.variable.VariableType;

public class IllegalActionException extends RuntimeException {

    public IllegalActionException(String varName, VariableType varType, VariableType legalType) {
        super("Action for type \"" + legalType.toString() + "\" is not legal for variable \"" + varName + "\" with type \"" + varType + "\"");
    }
}
