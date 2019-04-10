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

    public Token getNextToken(RandomAccessFile file , int lineNumber){
        int input;
        State currentState  = start;
        try {
            while ((input = file.read()) != -1) {
                if (input == '\n') {
                    lineNumber++;
                }
                State newState = map.get(new Transition(start,InputType.getTypeBychar((char) input)));
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
                        out = new Token(new StringBuilder().append(currentState.getStr()).append((char) input).toString(), newState.getTokenType());
                    }
                    if(out.getTokenType().equals(TokenType.IDENTIFIYER)){
                        if(symbolTable.contains(out.getToken())){
                            out.setTokenType(TokenType.KEYWORD);
                        }
                    }
                    return out;
                }
                else{
                   newState.setStr(new StringBuilder().append(currentState.getStr()).append((char) input).toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
