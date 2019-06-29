package icg;

import semantic.AddressGenerator;
import semantic.SymbolTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ICG {
    private static final int MAX_LINE_NUMBER = 500;
    private AddressGenerator addressGenerator;
    private int line;
    private Map<Integer,TAC> programBlock  = new HashMap<>();

    private SymbolTable symbolTable;

    public ICG(AddressGenerator addressGenerator) {
        this.addressGenerator = addressGenerator;
    }

    //    private Stack<> semanticStack;
//    public void func ( ){
//
//        ICGTokenType temp = ;
//        switch (temp) {
//
//            case MAIN:
//
//                break;
//            case INITIAL_MAIN:
//                break;
//            case MAINCHECKER:
//                break;
//            case AND_INITIAL_OUTPUTJUMPER:
//                break;
//            case POP:
//                break;
//            case JP_FIRST:
//                break;
//            case JP_END:
//                break;
//            case IF_JP:
//                break;
//            case IF_SAVE_JP:
//                break;
//            case IF_SAVE:
//                break;
//            case WHILE_VAR_SAVE:
//                break;
//            case WHILE_JP:
//                break;
//            case WHILE_JP_SAVE_FIRST_END:
//                break;
//            case RETURN:
//                break;
//            case PUSH_NULL:
//                break;
//            case SWITCH_VAR:
//                break;
//            case SWITCH_EXTRA_JP:
//                break;
//            case SWITH_END:
//                break;
//            case SWITCH_SAVE:
//                break;
//            case SWITCH_SAVE_JP:
//                break;
//            case PID:
//                break;
//            case CALING_FUNCTION:
//                break;
//            case ASSING_PUSH:
//                break;
//            case PUSH_ZERO:
//                break;
//            case PUSH_RELOP:
//                break;
//            case ADDOP:
//                break;
//            case MULT:
//                break;
//            case NOT:
//                break;
//            case PUSH_NUM:
//                break;
//            case INDEXING:
//                break;
//            case ASSING_TOFUNC:
//                break;
//            case OUTPUT_JUMPER_ASSIGN:
//                break;
//        }
//
//    }
}
