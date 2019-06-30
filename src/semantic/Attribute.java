package semantic;

public class Attribute {

    private String name;
    private int address;
    private AttributeType attributeType;

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


}
