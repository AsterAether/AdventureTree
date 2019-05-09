package at.asteraether.adventuretree.exceptions;

public class VariableNotFoundException extends RuntimeException {
    public VariableNotFoundException(String varName) {
        super("Variable \"" + varName + "\" not found");
    }
}
