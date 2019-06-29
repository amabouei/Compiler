package semantic;

import icg.ICG;
import lexical.Token;
import parser.Edge;
import parser.error.Error;

import java.util.LinkedList;
import java.util.Stack;

public class Semantic {

    private SymbolTable curSymbolTable;
    private AddressGenerator addressGenerator;

    private LinkedList<Error> errors = new LinkedList<>();

    public Semantic(SymbolTable curSymbolTable, AddressGenerator addressGenerator) {
        this.curSymbolTable = curSymbolTable;
        this.addressGenerator = addressGenerator;
    }

//    public void action(Edge edge, Token curToken) {
//        switch (edge.getSemanticTokenType()) {
//            case NOT_VOID:
//                notVoid();
//                break;
//            case CREATE_VAR:
//                createVar();
//                break;
//            case ASSIGN_NAME:
//                assignName(varName);
//                break;
//            case FIND_ID:
//                findID(varName);
//                break;
//            case VOID:
//                newVoid();
//                break;
//            case IS_NUMERIC:
//                isNumeric(varName);
//                break;
//            case ID_IS_NUMERIC:
//                idIsNumeric(varName);
//                break;
//            case RETURN_ADDRESS:
//                break;
//            case NUMERIC:
//                // is redundant
//                break;
//            case NEW_SCOPE:
//                addSymbolTable();
//                break;
//            case END_OF_SCOPE:
//                endScope();
//                break;
//            case BREAK:
//                findWhereToBreakOut();
//                break;
//            case CONTINUE:
//                findWhile();
//                break;
//            case WHILE:
//                addWhile();
//                break;
//            case END_OF_WHILE:
//                // redundant
//                break;
//            case SWITCH:
//                addSwitch();
//                break;
//            case END_OF_SWITCH:
//                // redundant
//                break;
//            case BEGIN_ARGS:
//                // must somehow count the number of args, between $beginArgs and $endOfArgs. Each $arg must increment the number of args
//                createTempCounter();
//                break;
//            case END_ARGS:
//                checkTempCounterValue();
//                break;
//            case ARG:
//                decrementTempCounter();
//                break;
//            case NUM:
//                // redundant
//                break;
//            case INT:
//                newInt();
//                break;
//        }
//    }
//
//    private void checkTempCounterValue() {
//
//    }
//
//    private void decrementTempCounter() {
//
//    }
//
//    private void createTempCounter() {
//    }
//
//    // need to create a new var of type 'int' but with a null name
//    private void newInt() {
//        Symbol newVar = new Symbol(SymbolType.INT, variableAddress);
//        curSymbolTable.defineNewVariable(newVar);
//        incrementVariableAddress();
//    }
//
//    private void addSwitch() {
//        curSymbolTable.defineNewVariable(new Symbol("switch", SymbolType.SWITCH));
//    }
//
//    private void addWhile() {
//        curSymbolTable.defineNewVariable(new Symbol("while", SymbolType.WHILE));
//    }
//
//    // must find a while in the current scope, or the father of the current scope
//    private void findWhile() {
//        if (curSymbolTable.findInSelfOrParent("while") == null) {
//            // exception
//        }
//    }
//
//    // must find a 'while' or 'switch' in the current scope (just for while), or the father of the current scope to break out of
//    private void findWhereToBreakOut() {
//        Symbol toWhile = curSymbolTable.findInSelfOrParent("while");
//        Symbol toSwitch = curSymbolTable.findInParent("switch");
//        if (toSwitch == null && toWhile == null) {
//            // exception
//        }
//    }
//
//    // must move the curSymbolTable to be its father
//    private void endScope() {
//        curSymbolTable = curSymbolTable.getParent();
//    }
//
//    // must add a new child, and then move the curSymbolTable to be the child
//    private void addSymbolTable() {
//        SymbolTable newScope = new SymbolTable(curSymbolTable);
//        curSymbolTable.defineNewScope(newScope);
//        curSymbolTable = newScope;
//    }
//
//    // must check to see if the variable 'id' is int or not.
//    // TODO: Must also handle the case when 'id' is referring to a function name
//    private void idIsNumeric(String id) {
//        Symbol id_symbol = curSymbolTable.find(id);
//        if (id_symbol.getSymbolType() != SymbolType.INT) {
//            //TODO: throw the appropriate exception
//        }
//    }
//
//    //probably redundant
//    private void isNumeric(String id) {
//
//    }
//
//    // need to create a new var of type 'void' but with a null name
//    private void newVoid() {
//        Symbol newVar =  new Symbol(SymbolType.VOID, variableAddress);
//        curSymbolTable.defineNewVariable(newVar);
//    }
//
//    // id must exist
//    private void findID(String id) {
//        curSymbolTable.find(id); // will throw an exception
//    }
//
//    // varName is the name that should be assigned
//    private void assignName(String name) {
//        Symbol var = curSymbolTable.find("");
//        var.setName(name);
//    }
//
//    // must find and change the latest var that has a type specified but the name is null.
//    // REDUNDANT considering newInt, newVoid
//    private void createVar() {
//
//    }
//
//    // must check to see if the latest var (the symbol before the '[' symbol) in the symbolTable is not of type void
//    private void notVoid() {
//        Symbol id = curSymbolTable.getContents().get(curSymbolTable.getContents().size() - 2);
//        if (id.getSymbolType() == SymbolType.VOID)
//            // throw an exception
//            return;
//    }

}
