package icg;

import java.util.StringJoiner;

public class Data {

    private int value;
    private boolean isConstant = true;
    private String label; ///for < ==
    private boolean isPointer;

    public Data(String label) {
        this.label = label;
    }

    public Data(int value) {
        this.value = value;
    }

    public Data(int value, boolean isConstant) {
        this.value = value;
        this.isConstant = isConstant;
    }

    public Data(int value, boolean isConstant, boolean isPointer) {
        this.value = value;
        this.isConstant = isConstant;
        this.isPointer = isPointer;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (isPointer) {
            return "@" + String.valueOf(value);
        }
        if (!isConstant) {

            return String.valueOf(value);
        } else
            return "#" + String.valueOf(value);
    }

    public String getWithAddress() {
        if (!isConstant)
            return "@" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    public void setPointer(boolean pointer) {
        isPointer = pointer;
    }

    public void setConstant(boolean constant) {
        isConstant = constant;
    }
}
