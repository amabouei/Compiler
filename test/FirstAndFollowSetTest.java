import org.junit.Test;
import parser.*;

import java.util.HashMap;
import java.util.Set;

public class FirstAndFollowSetTest {
    @Test
    public void firstSetTest() {
        FirstAndFollowSet firstAndFollowSet = new FirstAndFollowSet();
        HashMap<String, Set<String>> followSets = firstAndFollowSet.getFollowSets();
        for (String nonTerminal : followSets.keySet()) {
            System.out.print("Follow(" + nonTerminal + ") = { ");
            for (String s : followSets.get(nonTerminal)) {
                System.out.print(s + " ");
            }
            System.out.println(" }\n");
        }
    }
}
