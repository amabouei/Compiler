package icg;

public class Data {

    private int value;
    private boolean isConstant = true;
    private String label; ///for < ==

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


    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
