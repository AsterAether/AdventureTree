package at.asteraether.adventuretree.adventure.variable;

import java.io.Serializable;

public class Variable implements Serializable {

    private String name;
    private String value;
    private String defaultValue;

    private VariableType type;

    public Variable(String name, VariableType type, String value) throws NumberFormatException {
        this.name = name;
        this.type = type;
        if (value == null) {
            switch (type) {
                case TEXT:
                    this.value = "";
                    this.defaultValue = "";
                    break;
                case NUMBER:
                    this.value = "0";
                    this.defaultValue = "0";
                    break;
            }
        } else if (type == VariableType.NUMBER) {
            this.value = String.valueOf(Float.parseFloat(value));
            this.defaultValue = this.value;
        } else {
            this.value = value;
            this.defaultValue = this.value;
        }
    }

    public Variable(String name, VariableType type) {
        this(name, type, null);
    }

    public String getName() {
        return name;
    }

    public String getValue(String format) {
        if (format == null) {
            return value;
        } else if (type == VariableType.NUMBER) {
            return String.format("%" + format + "f", asNumber());
        }
        return value;
    }

    public float asNumber() {
        return Float.parseFloat(value);
    }

    public VariableType getType() {
        return type;
    }

    public void setValue(String value) throws NumberFormatException {
        if (type == VariableType.NUMBER) {
            this.value = String.valueOf(Float.parseFloat(value));
        } else {
            this.value = value;
        }
    }

    public void reset() {
        value = defaultValue;
    }

    @Override
    public String toString() {
        return name + ": " + type;
    }
}
