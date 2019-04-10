package lexical_step;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class main {


    public static void main(String[] args) {
//       String x = [^abc];

        Map<String,Integer> map = new HashMap<>();

        LexicalAnalyzer lexical = initializeLexical();

        map.put("sd",12);
        System.out.println(map.get("sd"));

//        Scanner scanner =  new Scanner(System.in);
//        String file = "/Users/mohammadabouei/Desktop/Compiler_project/src/src/lexical_step/test.txt";
    }

    public static HashSet<String> createSymbolTables(){
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
        Map<Transition, State> map;
        State start = new State(0, false, false);
        LinkedList<State> states = new LinkedList<>();
        states.add(start);
        for (int i = 1; i <= 16; i++) {
            boolean isfinal = false;
            boolean needToBack = false;
            switch (i){
                case 2:
                    isfinal = true;
                    needToBack = true;
                    break;
                case 4:
                    isfinal = true;
                    needToBack = true;
                    break;
                case 6:
                    isfinal = true;
                    break;
                case 7:
                    isfinal = true;
                    needToBack = true;
                    break;
                case 8:
                    isfinal = true;
                    break;
                case 12:
                    isfinal = true;
                    break;
                case 14:
                    isfinal = true;
                    break;
                case 16:
                    isfinal = true;
                    break;
            }
            states.add(new State(i,isfinal,needToBack));
        }
        //todo transition.....

        return null;
    }
}