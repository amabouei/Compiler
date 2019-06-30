package semantic;

public class Attribute {

    private String name;
    private int address;
    private AttributeType attributeType;
    private int value;

    public Attribute(String name, int address,AttributeType attributeType) {
        this.name = name;
        this.address = address;
        this.attributeType = attributeType;
    }

    public String getName() {
        return name;
    }

    public int getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }
}
