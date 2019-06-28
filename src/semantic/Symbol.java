package semantic;

import lexical.TokenType;

import java.util.LinkedList;

public class Symbol {

    private String name;
    private LinkedList<Variable> attributes = new LinkedList<>();
    private SymbolType symbolType;

    public Symbol(String name, LinkedList<Variable> attributes, SymbolType symbolType) {
        this.name = name;
        this.attributes = attributes;
        this.symbolType = symbolType;
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
}
