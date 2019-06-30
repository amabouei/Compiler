package parser;

import icg.ICGTokenType;
import semantic.SemanticTokenType;

public class Edge {

    private boolean token = false;
    private String label;
    private State next;
    private SemanticTokenType semanticTokenType;



    private ICGTokenType icgTokenType;

    public Edge(boolean token, String label, State next) {
        this.token = token;
        this.label = label;
        this.next = next;
    }

    public Edge(boolean token, String label, State next, SemanticTokenType semanticTokenType, ICGTokenType icgTokenType) {
        this.token = token;
        this.label = label;
        this.next = next;
        this.semanticTokenType = semanticTokenType;
        this.icgTokenType = icgTokenType;
    }

    public State getNext() {
        return next;
    }

    public String getLabel() {
        return label;
    }

    public boolean isToken() {
        return token;
    }

    public boolean needForSemanticCheck(){
        return semanticTokenType != null;
    }

    public  boolean needForICGCheck(){
        return icgTokenType != null;
    }

    public SemanticTokenType getSemanticTokenType() {
        return semanticTokenType;
    }

    public ICGTokenType getIcgTokenType() {
        return icgTokenType;
    }
}
