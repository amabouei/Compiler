import lexical.Creator;
import lexical.LexicalAnalyzer;
import parser.Grammar;
import parser.Parser;
import parser.error.Error;
import parser.error.ErrorType;
import parser.treeStructure.Node;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.*;

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
        Parser parser = new Parser(new Grammar(), lexical);
        parser.parseTree();
        printTree(parser.getRoot());
        printError(parser.getErrors());



    }

    public static void printError(LinkedList<Error> errors) {
        for (int i = 0; i < errors.size(); i++) {
            int lineNumber = errors.get(i).getLine();
            LinkedList<Error> syntaxError = new LinkedList<>();
            LinkedList<Error> lexicalError = new LinkedList<>();
            while (i < errors.size() && lineNumber == errors.get(i).getLine()) {
                Error curError = errors.get(i);
                if (curError.getErrorType() == ErrorType.Lexical) {
                    lexicalError.add(curError);
                } else {
                    syntaxError.add(curError);
                }
                i++;
            }
            for (Error error : lexicalError) {
                System.out.print(error.getLine() + ", " + error.getErrorType() + ",  " + error.getStr() + "    ");
            }
            if (!lexicalError.isEmpty()) {
                System.out.println();
            }
            for (Error error : syntaxError) {
                System.out.print(error.getLine() + ", " + error.getErrorType() + ",  " + error.getStr() + "    ");
            }
            if (!syntaxError.isEmpty()) {
                System.out.println();
            }
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
