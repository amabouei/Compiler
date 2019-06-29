package parser;

import semantic.SemanticTokenType;
import semantic.Symbol;
import semantic.SymbolTable;
import semantic.SymbolType;

import java.util.LinkedList;
import java.util.Stack;

public class Edge {

    private boolean token = false;
    private String label;
    private State next;

    public Edge(boolean token, String label, State next) {
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
        semanticRoutine(semanticCheck.replace("$", " "), root);
    }

    private void semanticRoutine(String semanticCheck, SymbolTable curScope) {
        SymbolTable curSymbolTable = curScope;
        String grammarToken, semanticType;
        grammarToken = semanticCheck.split("\\s")[0];
        semanticType = semanticCheck.split("\\s")[1];
        SemanticTokenType semanticToken = SemanticTokenType.getSemanticToken(semanticType);
        if (semanticToken == null) {
            System.out.println("Couldn't find the semantic routine name!");
            return;
        }
        switch (semanticToken) {
            case NOT_VOID:
                notVoid(curSymbolTable);
                break;
            case CREATE_VAR:
                createVar(curSymbolTable);
                break;
            case ASSIGN_NAME:
                assignName(curSymbolTable, grammarToken);
                break;
            case FIND_ID:
                findID(curSymbolTable, grammarToken);
                break;
            case VOID:
                newVoid(curSymbolTable);
                break;
            case IS_NUMERIC:
                isNumeric(curSymbolTable, grammarToken);
                break;
            case ID_IS_NUMERIC:
                idIsNumeric(curSymbolTable, grammarToken);
                break;
            case NUMERIC:
                // is redundant
                break;
            case NEW_SCOPE:
                addSymbolTable(curSymbolTable);
                break;
            case END_OF_SCOPE:
                endScope(curSymbolTable);
                break;
            case BREAK:
                findWhereToBreakOut(curSymbolTable);
                break;
            case CONTINUE:
                findWhile(curSymbolTable);
                break;
            case WHILE:
                addWhile(curSymbolTable);
                break;
            case END_OF_WHILE:
                // redundant
                break;
            case SWITCH:
                addSwitch(curSymbolTable);
                break;
            case END_OF_SWITCH:
                // redundant
                break;
            case BEGIN_ARGS:
                // must somehow count the number of args, between $beginArgs and $endOfArgs. Each $arg must increment the number of args
                break;
            case END_ARGS:
                break;
            case ARG:
                break;
            case NUM:
                // redundant
                break;
            case INT:
                newInt(curSymbolTable);
                break;
        }
        curScope = curSymbolTable;
    }

    // need to create a new var of type 'int' but with a null name
    private void newInt(SymbolTable curScope) {
        curScope.defineNewVariable(new Symbol(SymbolType.INT));
    }

    private void addSwitch(SymbolTable curScope) {
        curScope.defineNewVariable(new Symbol("switch", SymbolType.SWITCH));
    }

    private void addWhile(SymbolTable curScope) {
        curScope.defineNewVariable(new Symbol("while", SymbolType.WHILE));
    }

    // must find a while in the current scope, or the father of the current scope
    private void findWhile(SymbolTable curScope) {
        if (curScope.findInSelfOrParent("while") == null) {
            // exception
        }
    }

    // must find a 'while' or 'switch' in the current scope (just for while), or the father of the current scope to break out of
    private void findWhereToBreakOut(SymbolTable curScope) {
        Symbol toWhile = curScope.findInSelfOrParent("while");
        Symbol toSwitch = curScope.findInParent("switch");
        if (toSwitch == null && toWhile == null) {
            // exception
        }
    }

    // must move the curScope to be its father
    private void endScope(SymbolTable curScope) {
        curScope = curScope.getParent();
    }

    // must add a new child, and then move the curScope to be the child
    private void addSymbolTable(SymbolTable curScope) {
        SymbolTable newScope = new SymbolTable(curScope);
        curScope.defineNewScope(newScope);
        curScope = newScope;
    }

    // must check to see if the variable 'id' is int or not
    private void idIsNumeric(SymbolTable curScope, String id) {
        LinkedList<Symbol> contents = curScope.getContents();
        Symbol id_symbol = curScope.find(id);
        if (id_symbol.getSymbolType() != SymbolType.INT) {
            //TODO: throw the appropriate exception
        }
    }

    //probably redundant
    private void isNumeric(SymbolTable curScope, String id) {

    }

    // need to create a new var of type 'void' but with a null name
    private void newVoid(SymbolTable curScope) {
        curScope.defineNewVariable(new Symbol(SymbolType.VOID));
    }

    // id must exist
    private void findID(SymbolTable curScope, String id) {
        curScope.find(id); // will throw an exception
    }

    // grammarToken is the name that should be assigned
    private void assignName(SymbolTable curScope, String name) {
        Symbol var = curScope.find("");
        var.setName(name);
    }

    // must find and change the latest var that has a type specified but the name is null.
    // REDUNDANT considering newInt, newVoid
    private void createVar(SymbolTable curScope) {

    }

    // must check to see if the latest var (the symbol before the '[' symbol) in the symbolTable is not of type void
    private void notVoid(SymbolTable curScope) {
        Symbol id = curScope.getContents().get(curScope.getContents().size() - 2);
        if (id.getSymbolType() == SymbolType.VOID)
            return;
            // throw an exception
    }
}
