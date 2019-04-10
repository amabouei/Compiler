package lexical_step;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class LexicalAnalyzer {

    private Set<String> symbolTable = new HashSet<>();
    private Map<Transition,State> map = new HashMap<>();
    private State start;

    public LexicalAnalyzer(Set<String> symbolTable, Map<Transition, State> map, State start) {
        this.symbolTable = symbolTable;
        this.map = map;
        this.start = start;
    }

    public Token getNextToken(RandomAccessFile file , Integer lineNumber){
        int input;
        State currentState  = start;
        try {
            int loop = 0;
            while (true) {
                input = file.read();


                if (input == '\n') {
                    lineNumber++;
                }
                State newState;
                if(input != -1 ) {
                     newState = map.get(new Transition(currentState, InputType.getTypeByChar((char) input)));
                }else{
                     newState = map.get(new Transition(currentState, InputType.EOF));
                }
                if(newState == null){
                    //TODO
                    return null;
                }
                if(newState.isFinal()){
                    Token out;
                    if(newState.isNeedBack()) {
                        file.seek(file.getFilePointer() - 1);
                        out = new Token(currentState.getStr(), newState.getTokenType());
                    }
                    else {
                        if(currentState.getStr() != null) {
                            out = new Token(new StringBuilder(currentState.getStr()).append((char) input).toString(), newState.getTokenType());
                        }else{
                            out = new Token(String.valueOf((char)input),newState.getTokenType());
                        }
                    }
                    if(out.getTokenType().equals(TokenType.IDENTIFIYER)){
                        if(symbolTable.contains(out.getToken())){
                            out.setTokenType(TokenType.KEYWORD);
                        }
                    }
                    return out;
                }
                else {
                    if(currentState.getStr() != null) {
                        newState.setStr(new StringBuilder(currentState.getStr()).append((char) input).toString());
                    }else{
                        newState.setStr(String.valueOf((char)input));
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
