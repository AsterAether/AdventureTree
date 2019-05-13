package at.asteraether.adventuretree.adventure.variable.action;

public class SubstractAction extends NumberAction {

    public SubstractAction(String varName, int num) {
        super(varName, num);
    }

    public SubstractAction(String varName, String numVarName) {
        super(varName, numVarName);
    }

    @Override
    protected float performNumber(float var, float var2) {
        return var - var2;
    }

    @Override
    public ActionType getActionType() {
        return ActionType.SUBSTRACT;
    }
}
