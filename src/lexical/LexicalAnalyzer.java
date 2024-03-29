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
    private RandomAccessFile file;
    public LexicalAnalyzer(Set<String> symbolTable, Map<Transition, State> map, State start) {
        this.symbolTable = symbolTable;
        this.map = map;
        this.start = start;
    }

    public void setFile(RandomAccessFile file) {
        this.file = file;
    }

    public Token getNextToken() throws IncompleteException, InvalidInputException {
        int input;
        State currentState = start;
        int startline = lineNumber;
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
                InputType type =  InputType.getTypeByChar((char) input);
                if (input == -1) {
                   type = InputType.EOF;
                }
                newState = map.get(new Transition(currentState,type));

                if (newState == null) {
                    if(type == InputType.OTHER){
                        if(currentState.getStr() != null)
                             throw new InvalidInputException(currentState.getStr() + String.valueOf((char)input),lineNumber);
                        throw new InvalidInputException(String.valueOf((char)input),lineNumber);
                    }else{
                        if(input != -1) {
                            file.seek(file.getFilePointer() - 1);
                        }
                        if(currentState.getId() == 9){
                            throw new IncompleteException("/",startline);
                        }else{
                            throw new IncompleteException("/*",startline);
                        }
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
