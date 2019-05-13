import org.junit.Test;
import parser.*;

import java.util.HashMap;
import java.util.Set;

public class FirstAndFollowSetTest {
    @Test
    public void followSetTest() {
        FirstAndFollowSet firstAndFollowSet = new FirstAndFollowSet();
        firstAndFollowSet.initSet(true);
        HashMap<String, Set<String>> followSets = firstAndFollowSet.getFollowSets();
        for (String nonTerminal : followSets.keySet()) {
            System.out.print("Follow(" + nonTerminal + ") = { ");
            for (String s : followSets.get(nonTerminal)) {
                System.out.print(s + " ");
            }
            System.out.println(" }\n");
        }
    }

    @Test
    public void firstSetTest() {
        FirstAndFollowSet firstAndFollowSet = new FirstAndFollowSet();
        firstAndFollowSet.initSet(false);
        HashMap<String, Set<String>> firstSets = firstAndFollowSet.getFirstSets();
        for (String nonTerminal : firstSets.keySet()) {
            System.out.print("First(" + nonTerminal + ") = { ");
            for (String s : firstSets.get(nonTerminal)) {
                System.out.print(s + " ");
            }
            System.out.println(" }\n");
        }
    }
}
