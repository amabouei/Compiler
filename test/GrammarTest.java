import org.junit.Test;
import parser.*;

import java.util.HashMap;
import java.util.Set;

public class GrammarTest {
    @Test
    public void followSetTest() {
        Grammar firstAndFollowSet = new Grammar();
        firstAndFollowSet.initSet(true);
        HashMap<String, Set<String>> followSets = firstAndFollowSet.getFollowSets();
        for (String nonTerminal : followSets.keySet()) {
            System.out.print("Follow(" + nonTerminal + ") = { ");
            for (String s : followSets.get(nonTerminal)) {
                System.out.print(s + " ");
            }
            System.out.println("}\n");
        }
    }

    @Test
    public void firstSetTest() {
        Grammar firstAndFollowSet = new Grammar();
        firstAndFollowSet.initSet(false);
        HashMap<String, Set<String>> firstSets = firstAndFollowSet.getFirstSets();
        for (String nonTerminal : firstSets.keySet()) {
            System.out.print("First(" + nonTerminal + ") = { ");
            for (String s : firstSets.get(nonTerminal)) {
                System.out.print(s + "");
            }
            System.out.println("}\n");
        }
    }

    @Test
    public void diagramTest() {
        Grammar grammar = new Grammar();
//        grammar.initDiagram();
        HashMap<String, Diagram> subDiagrams = grammar.getSubDiagrams();
        for (String nonTerminal : subDiagrams.keySet()) {
            for (Edge edge : subDiagrams.get(nonTerminal).getStart().getEdges()) {
                System.out.print(nonTerminal + " -> " + edge.getLabel());
                State curState = edge.getNext();
                while (!curState.isFinal()) {
                    System.out.print(" " + curState.getEdge().getLabel());
                    curState = curState.getEdge().getNext();
                }
                System.out.println("\n");
            }
        }
    }
}

