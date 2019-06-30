package semantic;

import icg.ICG;
import lexical.Token;
import parser.Edge;

import java.util.LinkedList;
import java.util.Stack;
import semantic.error.ErrorType;
import semantic.error.Error;

public class Semantic {

    private SymbolTable curSymbolTable;
    private AddressGenerator addressGenerator;

    private LinkedList<Error> errors = new LinkedList<>();

    private Stack<String> temporaryStack = new Stack<>();
    private Token curToken;

    public Semantic(SymbolTable curSymbolTable, AddressGenerator addressGenerator) {
        this.curSymbolTable = curSymbolTable;
        this.addressGenerator = addressGenerator;
    }

    public void action(Edge edge, Token token) {
        curToken = token;
        switch (edge.getSemanticTokenType()) {
            case NOT_VOID:
                notVoid();
                break;
            case INITIALARRAY:
                initialArray();
                break;
            case INITIALPOINTER:
                initialPointer();
                break;
            case INT_CREATEVAR:
                newInt();
                createVar();
                break;
            case INITIAL_VAR:
                initialVar();
                break;
            case CREATE_VAR:
                createVar();
                break;
            case PUSH:
                push();
                break;
            case POP2:
                doublePop();
                break;
            case CREATEFUNCTION:
                createFunction();
                break;
            case VOID:
                newVoid();
                break;
            case DEFMAIN:
                defMain();
                break;
            case SWITCH:
                addSwitch();
                break;
            case NEW_SCOPE:
                addSymbolTable();
                break;
            case END_OF_SCOPE:
                endScope();
                break;
            case INT:
                newInt();
                break;
            case WHILE:
                addWhile();
                break;
            case BREAK:
                breakRoutine();
                break;
            case CONTINUE:
                continueRoutine();
                break;

//            case ASSIGN_NAME:
//                assignName(varName);
//                break;
//            case FIND_ID:
//                findID(varName);
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

//            case BREAK:
//                findWhereToBreakOut();
//                break;
//            case CONTINUE:
//                findWhile();
//                break;

//            case END_OF_WHILE:
//                // redundant
//                break;

//            case END_OF_SWITCH:
//                // redundant
//                break;
            case BEGIN_ARGS:
                // must somehow count the number of args, between $beginArgs and $endOfArgs. Each $arg must increment the number of args
                createTempCounter();
                break;
            case END_ARGS:
                checkTempCounterValue();
                break;
            case ARG:
                decrementTempCounter();
                break;
            case ID_IS_NUMERIC:
                checkIDIsNumeric();
                break;
//            case NUM:
//                // redundant
//                break;

        }
    }

    private void checkIDIsNumeric() {
        String name = curToken.getToken();
        Attribute var = curSymbolTable.find(name);
        if (var == null) {
            // TODO: variable not defined exception
        }
        if (!var.getAttributeType().equals(AttributeType.INT)) {
            // TODO: variable is not numeric exception
        }
    }

    private void createFunction() {
        String name = temporaryStack.pop();
        AttributeType attributeType = AttributeType.getTypeByName(temporaryStack.pop());

        SymbolTable newSymbolTable = new SymbolTable(curSymbolTable, name, SymbolTableType.FUNCTION);

        //jumper
        newSymbolTable.defineNewAttribute(new Attribute(name, addressGenerator.getVar(), AttributeType.INT));
        //return value
        newSymbolTable.defineNewAttribute(new Attribute(name, addressGenerator.getVar(), attributeType));
        curSymbolTable = newSymbolTable;
    }

    private void defMain() {
        SymbolTable main = curSymbolTable.getFunction("main");
        if (main != null) {
            if (main.getContents().size() > 2) {
                //TODO ...
            }
        } else {
            //TODO...
            errors.addFirst(new Error(ErrorType.MAIN_NOT_FOUND));
        }
    }

    private void doublePop() {
        temporaryStack.pop();
        temporaryStack.pop();
    }

    private void push() {
        temporaryStack.push(curToken.getToken());
    }

    private void endScope() {
        if (curSymbolTable.getParent() != null) {
            curSymbolTable = curSymbolTable.getParent();
        }
    }

    private void addSymbolTable() {
        SymbolTable newScope = new SymbolTable(curSymbolTable);
        curSymbolTable.defineNewScope(newScope);
        curSymbolTable = newScope;
    }

    private void addSymbolTable(SymbolTable newScope) {
        curSymbolTable.defineNewScope(newScope);
        curSymbolTable = newScope;
    }

    private void addSwitch() {
        SymbolTable newSymbolTable = new SymbolTable(curSymbolTable);
        newSymbolTable.defineNewAttribute(new Attribute("break", addressGenerator.getVar(), AttributeType.POINTER));
        addSymbolTable(newSymbolTable);
    }

    private void addWhile() {
        SymbolTable newSymbolTable = new SymbolTable(curSymbolTable);
        newSymbolTable.defineNewAttribute(new Attribute("break", addressGenerator.getVar(), AttributeType.POINTER));
        addSymbolTable(newSymbolTable);
    }


    private void notVoid() {
        String temp = temporaryStack.get(temporaryStack.size() - 2);
        if (temp.equals("void")) {
            errors.add(new Error(curToken.getLine(), ErrorType.ILLEGAL_TYPE_OF_VOID));
            //TODO temp = null and push
        } else {
            temporaryStack.push(curToken.getToken());
        }
    }

    private void initialVar() {
        String name = temporaryStack.pop();
        AttributeType attributeType = AttributeType.getTypeByName(temporaryStack.pop());
        if (attributeType != null) {
            curSymbolTable.defineNewAttribute(new Attribute(name, addressGenerator.getVar(), attributeType));
        }
    }


    private void newInt() {
        temporaryStack.push("int");
    }


    private void newVoid() {
        temporaryStack.push("void");
    }

    public void initialArray() {
        int size = Integer.parseInt(temporaryStack.pop());
        //TODO exception for a[3.4] isn't important
        String name = temporaryStack.pop();
        AttributeType attributeType = AttributeType.getTypeByName(temporaryStack.pop());
        if (attributeType == AttributeType.INT) {
            curSymbolTable.defineNewAttribute(new Attribute(name, addressGenerator.getArray(size), AttributeType.INT));
        }
    }

    public void initialPointer() {
        temporaryStack.pop();
        String name = temporaryStack.pop();
        AttributeType attributeType = AttributeType.getTypeByName(temporaryStack.pop());
        if (attributeType == AttributeType.INT) {
            curSymbolTable.defineNewAttribute(new Attribute(name, addressGenerator.getVar(), AttributeType.POINTER));
        }
    }

    private void createVar() {
        String name = curToken.getToken();
        if (curSymbolTable.findInSelfOrParent(name) != null) {
            // TODO: 6/30/19 handle error

            temporaryStack.push(null);
            return;
        }
        temporaryStack.push(name);
    }


    public void breakRoutine(){
        if (!curSymbolTable.isExistAppropriateBlockForBreak()) {
            errors.add(new Error(curToken.getLine(), ErrorType.NO_WHILE_OR_SWITCH_FOR_BREAK));
        }
    }


    public void continueRoutine(){
        if(!curSymbolTable.isExistAppropriateBlockForContinue()){
            errors.add(new Error(curToken.getLine(), ErrorType.NO_WHILE_FOR_CONTINUE));
        }
    }

    private void checkTempCounterValue() {
        int counter = Integer.parseInt(temporaryStack.get(temporaryStack.size() - 1));
        temporaryStack.pop();
        if (counter != 0) {
            errors.add(new Error(curToken.getLine(), ErrorType.MISMATCHED_NUMBER_OF_ARGUMENTS));
        }
    }

    private void decrementTempCounter() {
        int counter = Integer.parseInt(temporaryStack.get(temporaryStack.size() - 1));
        temporaryStack.pop();
        temporaryStack.push(String.valueOf(counter - 1));
    }

    private void createTempCounter() {
        SymbolTable func = curSymbolTable.getFunction(temporaryStack.get(temporaryStack.size() - 2));
        int numOfParameters = func.getContents().size() - 2; // shouldn't count return address and return value
        temporaryStack.push(String.valueOf(numOfParameters));
    }
//
//    // need to create a new var of type 'int' but with a null name

//
//    
//

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

//
//    // must add a new child, and then move the curSymbolTable to be the child
//
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


//
//    // must check to see if the latest var (the symbol before the '[' symbol) in the symbolTable is not of type void


}
