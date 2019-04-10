package lexical;

import lexical.exception.IncompleteException;
import lexical.exception.InvalidInputException;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class LexicalAnalyzer {

    private Set<String> symbolTable = new HashSet<>();
    private Map<Transition, State> map = new HashMap<>();
    private State start;
    private int lineNumber = 1;

    public LexicalAnalyzer(Set<String> symbolTable, Map<Transition, State> map, State start) {
        this.symbolTable = symbolTable;
        this.map = map;
        this.start = start;
    }

    public Token getNextToken(RandomAccessFile file) throws IncompleteException, InvalidInputException {
        int input;
        State currentState = start;

        try {
            int loop = 0;
            while (true) {
                input = file.read();
                State newState;
                if(input == '\n'){
                    if(currentState.getStr() == null || currentState.getId() == 13
                            ||currentState.getId() == 10 || currentState.getId() == 11 ) {
                        lineNumber++;
                    }
                }
                if (input != -1) {
                    newState = map.get(new Transition(currentState, InputType.getTypeByChar((char) input)));
                } else {
                    newState = map.get(new Transition(currentState, InputType.EOF));
                }
                if (newState == null) {
                    if(input == -1){
                        throw new IncompleteException();
                    }else{
                        if(currentState.getStr() != null)
                            throw new InvalidInputException(currentState.getStr()+ String.valueOf((char)input),lineNumber);
                        else
                            throw new InvalidInputException(String.valueOf((char)input),lineNumber);
                    }
                }
                if (newState.isFinal()) {
                    Token out;
                    if (newState.isNeedBack()) {
                        if(input != -1) {
                            file.seek(file.getFilePointer() - 1);
                        }
                        out = new Token(currentState.getStr(), newState.getTokenType(),lineNumber);
                    } else {
                        if (currentState.getStr() != null) {
                            out = new Token(currentState.getStr() + (char) input, newState.getTokenType(),lineNumber);
                        } else {
                            out = new Token(String.valueOf((char) input), newState.getTokenType(),lineNumber);
                        }
                    }
                    if (out.getTokenType().equals(TokenType.ID)) {
                        if (symbolTable.contains(out.getToken())) {
                            out.setTokenType(TokenType.KEYWORD);
                        }
                    }
                    return out;
                } else {

                    if (currentState.getStr() != null) {
                        newState.setStr(currentState.getStr() + (char) input);
                    } else {
                        newState.setStr(String.valueOf((char) input));
                    }
                    currentState = newState;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
