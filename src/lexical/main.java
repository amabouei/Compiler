package lexical;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class main {


    public static void main(String[] args) {
//       String x = [^abc];

        Map<String, Integer> map = new HashMap<>();

        LexicalAnalyzer lexical = initializeLexical();

        Integer lineNumber = 1;
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(Paths.get(System.getProperty("user.dir"), "src", "test.txt").toString(), "r");
            int lastLine = 0;
            int loop = 0;
            while (true) {
                loop++;
//                accessFile.seek(accessFile.getFilePointer() - 1); /// chert......
                Token next = lexical.getNextToken(accessFile, lineNumber);
                if (next.getTokenType() != TokenType.EOF && next.getTokenType() != TokenType.WHITESPACE) {
                    if (lastLine != lineNumber) {
                        System.out.print("\n" + lineNumber + "-" + " ");
                        lastLine = lineNumber;
                        System.out.print(next.toString());
                    } else {
                        System.out.print(" ");
                        System.out.print(next.toString());
                    }
                }
                if (next.getTokenType() == TokenType.WHITESPACE && next.getToken().equals("\n")) {
                    lineNumber++;
                }
                if (next.getTokenType() == TokenType.EOF) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static HashSet<String> createSymbolTables() {
        HashSet<String> set = new HashSet<>();
        set.add("if");
        set.add("else");
        set.add("void");
        set.add("int");
        set.add("while");
        set.add("break");
        set.add("continue");
        set.add("switch");
        set.add("default");
        set.add("case");
        set.add("return");
        return set;
    }

    public static LexicalAnalyzer initializeLexical() {

        HashSet<String> symbolTable = createSymbolTables();
        Map<Transition, State> map = new HashMap<>();
        State start = new State(0, false, false, null);
        State[] states = new State[19];
        states[0] = start;
        for (int i = 1; i <= 18; i++) {
            boolean isfinal = false;
            boolean needToBack = false;
            TokenType tokenType = null;
            switch (i) {
                case 2:
                    isfinal = true;
                    needToBack = true;
                    tokenType = TokenType.IDENTIFIER;
                    break;
                case 4:
                    isfinal = true;
                    needToBack = true;
                    tokenType = TokenType.NUMBER;
                    break;
                case 6:
                    isfinal = true;
                    tokenType = TokenType.SYMBOL;
                    break;
                case 7:
                    isfinal = true;
                    needToBack = true;
                    tokenType = TokenType.SYMBOL;
                    break;
                case 8:
                    isfinal = true;
                    tokenType = TokenType.SYMBOL;
                    break;
                case 12:
                    isfinal = true;
                    tokenType = TokenType.COMMENT;
                    break;
                case 14:
                    isfinal = true;
                    tokenType = TokenType.COMMENT;
                    break;
                case 16:
                    isfinal = true;
                    needToBack = true;
                    tokenType = TokenType.WHITESPACE;
                    break;
                case 17:
                    isfinal = true;
                    tokenType = TokenType.WHITESPACE;
                    break;
                case 18:
                    isfinal = true;
                    tokenType = TokenType.EOF;
                    break;
            }
            states[i] = new State(i, isfinal, needToBack, tokenType);
        }

        //state0
        map.put(new Transition(start, InputType.LETTER), states[1]);
        map.put(new Transition(start, InputType.DIGIT), states[3]);
        map.put(new Transition(start, InputType.SYMBOL), states[8]);
        map.put(new Transition(start, InputType.EQUALS), states[5]);
        map.put(new Transition(start, InputType.SLASH), states[9]);
        map.put(new Transition(start, InputType.WHITESPACE), states[15]);
        map.put(new Transition(start, InputType.END_LINE), states[17]);
        map.put(new Transition(start, InputType.EOF), states[18]);

        //state1
        map.put(new Transition(states[1], InputType.LETTER), states[1]);
        map.put(new Transition(states[1], InputType.DIGIT), states[1]);

        for (InputType value : InputType.values()) {
            if (value != InputType.LETTER && value != InputType.DIGIT && value != InputType.OTHER) {
                map.put(new Transition(states[1], value), states[2]);
            }
        }
        //state3
        map.put(new Transition(states[3], InputType.DIGIT), states[3]);

        for (InputType value : InputType.values()) {
            if (value != InputType.DIGIT && value != InputType.OTHER) {
                map.put(new Transition(states[3], value), states[4]);
            }
        }

        //state5
        map.put(new Transition(states[5], InputType.EQUALS), states[6]);

        for (InputType value : InputType.values()) {
            if (value != InputType.EQUALS && value != InputType.OTHER) {
                map.put(new Transition(states[5], value), states[7]);
            }
        }


        //state9
        map.put(new Transition(states[9], InputType.STAR), states[10]);
        map.put(new Transition(states[9], InputType.SLASH), states[13]);

        //state10
        map.put(new Transition(states[10], InputType.STAR), states[11]);

        for (InputType value : InputType.values()) {
            if (value != InputType.STAR && value != InputType.EOF) {
                map.put(new Transition(states[10], value), states[10]);
            }
        }

        //tate11
        map.put(new Transition(states[11], InputType.STAR), states[11]);
        map.put(new Transition(states[11], InputType.SLASH), states[12]);
        for (InputType value : InputType.values()) {
            if (value != InputType.STAR && value != InputType.SLASH && value != InputType.EOF) {
                map.put(new Transition(states[11], value), states[10]);
            }
        }


        //state13
        map.put(new Transition(states[13], InputType.END_LINE), states[13]);
        for (InputType value : InputType.values()) {
            if (value != InputType.END_LINE && value != InputType.EOF) {
                map.put(new Transition(states[13], value), states[13]);
            }
        }

        //state15
        map.put(new Transition(states[15], InputType.END_LINE), states[15]);
        map.put(new Transition(states[15], InputType.WHITESPACE), states[15]);

        for (InputType value : InputType.values()) {
            if (value != InputType.END_LINE && value != InputType.WHITESPACE) {
                map.put(new Transition(states[15], value), states[16]);
            }
        }
        return new LexicalAnalyzer(symbolTable, map, start);
    }
}