package semantic;

public class Variable {
    private SemanticTokenType type;
    private String name;
    private int address;

    public Variable(SemanticTokenType type, String name, int address) {
        this.type = type;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getAddress() {
        return address;
    }
}
