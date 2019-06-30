package parser;

import icg.ICGTokenType;
import semantic.Semantic;
import semantic.SemanticTokenType;
import semantic.SymbolTable;

import java.util.ArrayList;
import java.util.Stack;

public class Diagram {
    private String name;
    private State start;
    private State finalState;

    public Diagram(String name) {
        this.name = name;
        start = new State();
        finalState = new State(true);
    }

    public String getName() {
        return name;
    }

    public State getStart() {
        return start;
    }

    public void addRule(ArrayList<String> rule) {
        State curState = start;
        for (int i = 0; i < rule.size(); i++) {
            String step = rule.get(i);
            SemanticTokenType semanticTokenType = null;
            ICGTokenType icgTokenType = null;
            boolean isFinal = false;

            if(i + 1 < rule.size()  && rule.get(i + 1).startsWith("$")) {
                semanticTokenType = SemanticTokenType.getSemanticToken(rule.get(i + 1).replace("$",""));
                i++;
                if( i + 1 < rule.size() && rule.get(i + 1).startsWith("#") ){
                    icgTokenType = ICGTokenType.getTokenByName(rule.get(i+1).replace("#",""));
                    i++;
                }
            }else{
                if(i + 1 < rule.size()  && rule.get(i + 1).startsWith("")){
                    if( i + 1 < rule.size() && rule.get(i + 1).startsWith("#") ){
                        icgTokenType = ICGTokenType.getTokenByName(rule.get(i+1).replace("#",""));
                        i++;
                    }
                }
            }

            if (i == rule.size() - 1)
                isFinal = true;

            boolean isNonTerminal = step.charAt(0) <= 90 && step.charAt(0) >= 65;
            Edge newEdge;
            if (!isFinal)
                newEdge = new Edge(!isNonTerminal, step, new State(),semanticTokenType,icgTokenType);
            else
                newEdge = new Edge(!isNonTerminal, step, finalState,semanticTokenType,icgTokenType);
            curState.addEdge(newEdge);
            curState = newEdge.getNext();
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Diagram diagram = (Diagram) obj;
        return diagram.getName().equals(name);
    }
}
