package parser;

import semantic.SemanticTokenType;
import semantic.SymbolTable;

import java.util.Stack;

public class Edge {

    private boolean token = false;
    private String label;
    private State next;

    public Edge (boolean token, String label, State next) {
        this.token = token;
        this.label = label;
        this.next = next;
    }

    public State getNext() {
        return next;
    }

    public String getLabel() {
        return label;
    }

    public boolean isToken() {
        return token;
    }

    public void run(String semanticCheck, SymbolTable root, Stack semanticStack) {
        boolean isICT = semanticCheck.startsWith("#");
//        if (isICT)
//            generateIntermediateCode(semanticCheck.replace("#", ""), semanticStack); TODO: must also take a file to write the codes into
//        else
            semanticRoutine(semanticCheck.replace("$", ""), root);
    }

    private void semanticRoutine(String semanticCheck, SymbolTable root) {
        SymbolTable curSymbolTable = root;
        SemanticTokenType semanticToken = SemanticTokenType.getSemanticToken(semanticCheck);
        switch (semanticToken) {
            // TODO: in some cases, there's the need to add a new symbol table. and in others it's just needed to add a symbol to the current table
            case SOME_TOKEN:
                break;
        }

        root = curSymbolTable;
    }


}
