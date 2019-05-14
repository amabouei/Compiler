package parser;

import java.util.ArrayList;
import java.util.Stack;

public class State {

    private boolean isFinal = false;
    private ArrayList<Edge> edges = new ArrayList<>();

    public State() {

    }

    public State(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public Edge getEdge() {
        return edges.get(0);
    }

    //    private State getOutput(String input) {
//
//    }

}
