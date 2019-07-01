package semantic;

import lexical.Token;
import parser.Diagram;
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

    private boolean tempForExpression = false;

    public Semantic(SymbolTable curSymbolTable, AddressGenerator addressGenerator) {
        this.curSymbolTable = curSymbolTable;
        this.addressGenerator = addressGenerator;
    }

    public void action(SemanticTokenType semanticTokenType, Token token, Diagram curDiagram) {

//        for (String s : temporaryStack) {
//            System.out.println(s);
//        }

//        System.out.println("    " + curDiagram.getName() + "----------------------------" + "  " + edge.getSemanticTokenType());
        curToken = token;
        if (semanticTokenType == null)
            return;
        switch (semanticTokenType) {
            case DEFMAIN:
                defMain();
                break;
            case CREATEVAR:
                createVar();
                break;
            case INITIAL_VAR:
                initialVar();
                break;
            case NOTVOID:
                notVoid();
                break;
            case INITIALARRAY:
                initialArray();
                break;
            case INT:
                newInt();
                break;
            case VOID:
                newVoid();
                break;
            case CREATEFUNCTION:
                createFunction();
                break;
            case PUSH:
                push();
                break;
            case INT_CREATEVAR:
                newInt();
                createVar();
                break;
            case POP2:
                doublePop();
                break;
            case INITIALPOINTER:
                initialPointer();
                break;
            case NEWSCOPE:
                addSymbolTable();
                break;
            case ENDOFSCOPE:
                endScope();
                break;
            case CONTINUE:
                continueRoutine();
                break;
            case BREAK:
                breakRoutine();
                break;
            case WHILE:
                addWhile();
                break;
            case SWITCH:
                addSwitch();
                break;
            case EXPERSSIONISNUMBERIC:
                expressionIsNumeric();
                break;
            case EXPERSSIONRESET:
                expressionReset();
                break;
            case BEGINARGS:
                createTempCounter();
                break;
            case IDISNUMBERIC:
                checkIDIsNumeric();
                break;
            case ENDARGS:
                checkTempCounterValue();
                break;
            case ARG:
                decrementTempCounter();
                break;
            case BACK:
                back();
                break;
        }
    }


    private void back(){
        curSymbolTable = curSymbolTable.getParent();
    }
    private void expressionIsNumeric() {
        if (!tempForExpression) {
            //todo
            errors.add(new Error(curToken.getLine(), ErrorType.ILLEGAL_TYPE_OF_VOID));
            // or :
//            errors.add(new Error(curToken.getLine(), ErrorType.TYPE_MISMATCH));
        }
    }

    private void expressionReset() {
        tempForExpression = true;
    }


    private void checkIDIsNumeric() {
        String name = temporaryStack.pop();
        Attribute var = curSymbolTable.find(name);
        if (var == null) {
            errors.add(new Error(curToken.getLine(), ErrorType.ID_NOT_DEFINED, name));
            // TODO: variable not defined exception
        } else if (!var.getAttributeType().equals(AttributeType.INT)) {
            // TODO: variable is not numeric exception
            errors.add(new Error(curToken.getLine(), ErrorType.TYPE_MISMATCH, name));
        }
    }

    private void createFunction() {
        String name = temporaryStack.pop();
        AttributeType attributeType = AttributeType.getTypeByName(temporaryStack.pop());
        SymbolTable newSymbolTable = new SymbolTable(curSymbolTable, name, SymbolTableType.FUNCTION);
        curSymbolTable.defineNewScope(newSymbolTable);
        //jumper
        newSymbolTable.defineNewAttribute(new Attribute(name + " return address", addressGenerator.getVar(), AttributeType.INT));
        //return value
        newSymbolTable.defineNewAttribute(new Attribute(name, addressGenerator.getVar(), attributeType));

        curSymbolTable = newSymbolTable;
    }

    private void defMain() {

        //todo check output type;
        SymbolTable main = curSymbolTable.getFunction("main");
        if (main != null) {
            if (main.getContents().size() > 2 || main.getContents().get(1).getAttributeType() != AttributeType.VOID ) {
                //TODO ...
                errors.addFirst(new Error(ErrorType.MAIN_NOT_FOUND));
            }
            return;
        }
        errors.addFirst(new Error(ErrorType.MAIN_NOT_FOUND));


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
        SymbolTable newScope = new SymbolTable(curSymbolTable,SymbolTableType.BLOCK);
        curSymbolTable.defineNewScope(newScope);
        curSymbolTable = newScope;
    }

    private void addSymbolTable(SymbolTable newScope) {
        curSymbolTable.defineNewScope(newScope);
        curSymbolTable = newScope;
    }

    private void addSwitch() {
        SymbolTable newSymbolTable = new SymbolTable(curSymbolTable,SymbolTableType.SWITCH);
        newSymbolTable.defineNewAttribute(new Attribute("break", addressGenerator.getVar(), AttributeType.POINTER));
        addSymbolTable(newSymbolTable);
    }

    private void addWhile() {
        SymbolTable newSymbolTable = new SymbolTable(curSymbolTable,SymbolTableType.WHILE);
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
            curSymbolTable.defineNewAttribute(new Attribute(name, addressGenerator.getArray(size), AttributeType.ARRAY));
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


    public void breakRoutine() {
        if (!curSymbolTable.isExistAppropriateBlockForBreak()) {
            errors.add(new Error(curToken.getLine(), ErrorType.NO_WHILE_OR_SWITCH_FOR_BREAK));
        }
    }


    public void continueRoutine() {
        if (!curSymbolTable.isExistAppropriateBlockForContinue()) {
            errors.add(new Error(curToken.getLine(), ErrorType.NO_WHILE_FOR_CONTINUE));
        }
    }

    private void checkTempCounterValue() {
        int counter = Integer.parseInt(temporaryStack.pop());
        if (counter != 0) {
            errors.add(new Error(curToken.getLine(), ErrorType.MISMATCHED_NUMBER_OF_ARGUMENTS));
        }
    }

    private void decrementTempCounter() {
        int counter = Integer.parseInt(temporaryStack.pop());
        temporaryStack.push(String.valueOf(counter - 1));
    }

    private void createTempCounter() {

        ///handle function name....
        String name = temporaryStack.pop();
//        SymbolTable func = curSymbolTable.getFunction(temporaryStack.get(temporaryStack.size() - 1));
        SymbolTable func = curSymbolTable.getFunction(name);
        if (func.getContents().get(1).getAttributeType() == AttributeType.VOID) {
            tempForExpression = false;
        }
        int numOfParameters = func.getContents().size() - 2; // shouldn't count return address and return value
        temporaryStack.push(String.valueOf(numOfParameters));
    }

    public LinkedList<Error> getErrors() {
        return errors;
    }


}
