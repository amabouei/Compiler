package parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class FirstAndFollowSet {
    private Set<String> nonTerminals = new HashSet<>();
    private HashMap<String, Set<String>> followSets = new HashMap<>();
    private HashMap<String, Set<String>> firstSets = new HashMap<>();
    public static final String followSetsFileAddress = Paths.get(System.getProperty("user.dir"),  "follow.txt").toString();
    public static final String firstSetsFileAddress = Paths.get(System.getProperty("user.dir"), "first.txt").toString();

    public HashMap<String, Set<String>> getFollowSets() {
        return followSets;
    }

    public HashMap<String, Set<String>> getFirstSets() {
        return firstSets;
    }

    public FirstAndFollowSet() {
    }

    public void initSet(boolean followSetFile) {
        String address;
        if (followSetFile)
            address = followSetsFileAddress;
        else
            address = firstSetsFileAddress;
        FileReader setsFile = null;
        try {
            setsFile = new FileReader(address);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (setsFile == null) {
            System.out.println("File not found");
            return;
        }

        Scanner scanner = new Scanner(setsFile);
        String curLine;
        while (scanner.hasNext()) {
            curLine = scanner.nextLine();
            String[] split = curLine.split("\\s");
            ArrayList<String> modified = new ArrayList<>();
            for (String word : split) {
                word = word.replaceFirst(",", "");
                modified.add(word);
            }
            String curNonTerminal = modified.get(0);
            nonTerminals.add(curNonTerminal);
            modified.remove(curNonTerminal);
            if (followSetFile) {
                Set<String> curFollowSet = new HashSet<>(modified);
                followSets.put(curNonTerminal, curFollowSet);
            }
            else {
                Set<String> curFirstSet = new HashSet<>(modified);
                firstSets.put(curNonTerminal, curFirstSet);
            }
        }
        try {
            setsFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

