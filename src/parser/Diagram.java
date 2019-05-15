package parser;

import java.util.ArrayList;

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
                        boolean isFinal = false;
                        if (i == rule.size() - 1)
                                isFinal = true;
                        boolean isNonTerminal = step.charAt(0) <= 90 && step.charAt(0) >= 65;
                        Edge newEdge;
                        if (!isFinal)
                                newEdge = new Edge(!isNonTerminal, step, new State());
                        else
                                newEdge = new Edge(!isNonTerminal, step, finalState);
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
