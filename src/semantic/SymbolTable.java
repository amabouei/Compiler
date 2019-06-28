package semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class SymbolTable {

    private SymbolTable parent;
    private LinkedList<SymbolTable> children;
//    private HashMap<String ,Symbol> symbols; ///mohem nist !
    private LinkedList<Symbol> contents;


    public Symbol find (String symbolName) {
        for (Symbol symbol : contents) {
            if (symbol.getName().equals(symbolName))
                return symbol;
        }
        if (parent != null) {
            return parent.find(symbolName);
        } else return null; // TODO: needs to be replaced with appropriate exception
    }

    public boolean contains (String syymbolName) {
        for (Symbol symbol : contents) {
            if (symbol.getName().equals(syymbolName))
                return true;
        }
        return false;
    }

    public void defineNewScope (SymbolTable newScope) {
        children.add(newScope);
    }

    public void defineNewVariable (Symbol symbol) {
        if (this.contains(symbol.getName()))
            return; // TODO: must throw an exception
        contents.add(symbol);
    }

}
