package at.asteraether.adventuretree.adventure.variable.action;

public class AddAction extends NumberAction {

    public AddAction(String numVarName, int num) {
        super(numVarName, num);
    }

    public AddAction(String varName, String numVarName) {
        super(varName, numVarName);
    }

    @Override
    protected float performNumber(float var, float var2) {
        return var + var2;
    }
}
