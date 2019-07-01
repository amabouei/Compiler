package parser;

import icg.ICG;
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
        int size = rule.size();
        ICGTokenType lastToken = null;
        if(rule.get(rule.size() - 1).startsWith("#")){
            lastToken = ICGTokenType.getTokenByName(rule.get(rule.size() - 1).replace("#", ""));
            size--;
        }
        for (int i = 0; i < size; i++) {
            String step = rule.get(i);
            SemanticTokenType semanticTokenType = null;
            ICGTokenType icgTokenType = null;
            boolean isFinal = false;

            if (step.startsWith("$")) {
                semanticTokenType = SemanticTokenType.getSemanticToken(step.replace("$", ""));
                i++;
                if (i < size && rule.get(i).startsWith("#")) {
                    icgTokenType = ICGTokenType.getTokenByName(rule.get(i).replace("#", ""));
                    i++;
                }
            } else {
                if (step.startsWith("#")) {
                    icgTokenType = ICGTokenType.getTokenByName(step.replace("#", ""));
                    i++;
                }

            }
            step = rule.get(i);

            if (i == size - 1)
                isFinal = true;

            boolean isNonTerminal = step.charAt(0) <= 90 && step.charAt(0) >= 65;

            Edge newEdge;
            if (!isFinal)
                newEdge = new Edge(!isNonTerminal, step, new State(), semanticTokenType, icgTokenType);
            else{
                newEdge = new Edge(!isNonTerminal, step, finalState, semanticTokenType, icgTokenType);
                if(lastToken != null){
                    newEdge.setAfterIcgTokenType(lastToken);
                }
            }

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
