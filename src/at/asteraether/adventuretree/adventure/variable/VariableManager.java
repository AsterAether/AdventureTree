package at.asteraether.adventuretree.adventure.variable;

import at.asteraether.adventuretree.exceptions.VariableNotFoundException;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableManager implements Serializable {

    private transient static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{(.*)}");

    private final Map<String, Variable> variableMap;

    public VariableManager() {
        variableMap = new HashMap<>();
    }


    public void addVariable(String name, VariableType type) {
        Variable var = new Variable(name, type);
        variableMap.put(name, var);
    }

    public void addVariable(String name, VariableType type, String startValue) {
        Variable var = new Variable(name, type, startValue);
        variableMap.put(name, var);
    }

    public Variable getVariable(String name) throws VariableNotFoundException {
        if (!variableMap.containsKey(name)) {
            throw new VariableNotFoundException(name);
        }
        return variableMap.get(name);
    }

    public void reset() {
        variableMap.values().forEach(Variable::reset);
    }

    public void clear() {
        variableMap.clear();
    }

    public Collection<Variable> getVariables() {
        return variableMap.values();
    }

    public String variablePass(String text) throws VariableNotFoundException {
        Matcher matcher = VARIABLE_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer(text.length());
        while (matcher.find()) {
            String varName = matcher.group(1);
            String format = null;
            if (varName.contains(":")) {
                String[] parts = varName.split(":");
                varName = parts[0];
                format = parts[1];
            }
            Variable var = getVariable(varName);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(var.getValue(format)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
