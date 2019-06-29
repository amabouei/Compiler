package semantic;

import lexical.TokenType;

import java.util.LinkedList;

public class Symbol {

    private String name = "";
    private LinkedList<Variable> attributes = new LinkedList<>();
    private SymbolType symbolType;
    private int address;

    public Symbol(String name, LinkedList<Variable> attributes, SymbolType symbolType) {
        this.name = name;
        this.attributes = attributes;
        this.symbolType = symbolType;
    }

    public Symbol(String name, SymbolType symbolType) {
        this.name = name;
        this.symbolType = symbolType;
    }

    public Symbol(SymbolType type) {
        this.symbolType = type;
    }

    public Symbol(SymbolType type, int variableAddress) {
        address = variableAddress;
        symbolType = type;
    }

    public void addAttribute (LinkedList<Variable> attributes) {
        this.attributes.addAll(attributes);
    }

    public void addAttribute (Variable attribute) {
        attributes.add(attribute);
    }

    public void addAttribute (TokenType type, String name) {
        int address = -1; // TODO: must find a new address for the variable. Must have a global indicator that shows the current address of the program.
        attributes.add(new Variable(SemanticTokenType.getSemanticToken(type.name()), name, address));
    }

    public String getName() {
        return name;
    }

    public SymbolType getSymbolType() {
        return symbolType;
    }

    public void setName(String name) {
        this.name = name;
    }
}
