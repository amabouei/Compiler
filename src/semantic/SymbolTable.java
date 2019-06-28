package semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class SymbolTable {

    private SymbolTable parent;
    private LinkedList<SymbolTable> childs;
    private HashMap<String ,Symbol> symbols; ///mohem nist !
}
