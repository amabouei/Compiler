package parser;

public class Edge {

    private boolean token = false;
    private String label;
    private State next;

    public Edge (boolean token, String label, State next) {
        this.token = token;
        this.label = label;
        this.next = next;
    }

    public State getNext() {
        return next;
    }

    public String getLabel() {
        return label;
    }
}
