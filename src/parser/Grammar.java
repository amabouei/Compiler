package parser;


import semantic.Semantic;
import semantic.SymbolTable;

import javax.sound.midi.Soundbank;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Grammar {
    private HashMap<String, Set<String>> followSets = new HashMap<>();
    private HashMap<String, Set<String>> firstSets = new HashMap<>();
    private HashMap<String, Diagram> subDiagrams = new HashMap<>();

    private static final String followSetsFileAddress = Paths.get(System.getProperty("user.dir"),  "follow.txt").toString();
    private static final String firstSetsFileAddress = Paths.get(System.getProperty("user.dir"), "first.txt").toString();
    private static final String grammarFileAddress = Paths.get(System.getProperty("user.dir"), "LL(1) Grammar.txt").toString();


    public Grammar() {
        initSet(true);
        initSet(false);
        initDiagram();
    }

    public HashMap<String, Set<String>> getFollowSets() {
        return followSets;
    }

    public HashMap<String, Set<String>> getFirstSets() {
        return firstSets;
    }

    public HashMap<String, Diagram> getSubDiagrams() {
        return subDiagrams;
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
                if (word.length() > 1)
                    word = word.replaceFirst(",", "");
                modified.add(word);
            }
            String curNonTerminal = modified.get(0);
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

    public void initDiagram() {
        FileReader grammarFile = null;
        try {
            grammarFile = new FileReader(grammarFileAddress);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (grammarFile == null) {
            System.out.println("Grammar File Not Found!");
            return;
        }
        Scanner scanner = new Scanner(grammarFile);
        String curLine;
        while (scanner.hasNext()) {
            curLine = scanner.nextLine().replace("->", " ");
            String[] split = curLine.replaceAll("\\s+", " ").split("\\s");
            String curNonTerminal = split[0];
            ArrayList<String> rule = new ArrayList<>(Arrays.asList(split));
            rule.remove(0);
            if (!subDiagrams.containsKey(curNonTerminal)) {
                subDiagrams.put(curNonTerminal, new Diagram(curNonTerminal));
            }
            subDiagrams.get(curNonTerminal).addRule(rule);
        }
    }
}

