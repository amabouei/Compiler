package semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class SymbolTable {

    private SymbolTable parent;
    private LinkedList<SymbolTable> children = new LinkedList<>();
//    private HashMap<String ,Symbol> symbols; ///mohem nist !
    private LinkedList<Symbol> contents = new LinkedList<>();

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public Symbol find(String symbolName) {
        for (Symbol symbol : contents) {
            if (symbol.getName().equals(symbolName))
                return symbol;
        }
        if (parent != null) {
            return parent.find(symbolName);
        } else return null; // TODO: needs to be replaced with appropriate exception
    }

    public Symbol findInContents(String symbolName) {
        for (Symbol symbol : contents) {
            if (symbol.getName().equals(symbolName))
                return symbol;
        }
        return null;
    }

    public Symbol findInSelfOrParent(String symbolName) {
        for (Symbol symbol : contents) {
            if (symbol.getName().equals(symbolName))
                return symbol;
        }
        if (parent != null) {
            return parent.findInContents(symbolName);
        }
        return null;
    }

    public Symbol findInParent(String symbolName) {
        return parent.findInContents(symbolName);
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

    public LinkedList<SymbolTable> getChildren() {
        return children;
    }

    public LinkedList<Symbol> getContents() {
        return contents;
    }

    public SymbolTable getParent() {
        return parent;
    }
}
