import lexical.Creator;
import lexical.LexicalAnalyzer;
import parser.Grammar;
import parser.Parser;
import parser.error.Error;
import parser.treeStructure.Node;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        LexicalAnalyzer lexical = Creator.initializeLexical();
        RandomAccessFile codeInput = null;
        try {
            codeInput = new RandomAccessFile(Paths.get(System.getProperty("user.dir"), "src", "test.txt").toString(), "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        lexical.setFile(codeInput);
        Parser parser = new Parser(new Grammar(),lexical);
        Node node = parser.parseTree();
        printTree(node);

        for (Error error : parser.getErrors()) {
            System.out.println(error.getLine()+"  "+error.getErrorType() +" "+error.getStr());
        }

    }

    public static void printTree(Node node){
        for (int height = node.getHeight(); height > 0; height--) {
            System.out.print("|  ");
        }
        System.out.println(node.getLabel());
        if(!node.isTerminal()) {
            for (Node child : node.getChildren()) {
                    printTree(child);
            }
        }
    }
}
