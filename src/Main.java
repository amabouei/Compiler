import icg.ICG;
import lexical.Creator;
import lexical.LexicalAnalyzer;
import outputGenerator.OutputGenerator;
import parser.Grammar;
import parser.Parser;
import semantic.*;
import semantic.error.Error;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = Creator.initializeLexical();
        RandomAccessFile codeInput = null;
        try {
            codeInput = new RandomAccessFile(Paths.get(System.getProperty("user.dir"), "src", "test.txt").toString(), "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        lexicalAnalyzer.setFile(codeInput);
        SymbolTable root = new SymbolTable(null, SymbolTableType.GLOBAL);
        AddressGenerator addressGenerator = new AddressGenerator();
        ICG icg = new ICG(addressGenerator);
        Semantic semantic = new Semantic(root, addressGenerator);
        Parser parser = new Parser(new Grammar(), lexicalAnalyzer, semantic, icg);
        parser.parseTree();

        //TODO file...

//        for (Error error : parser.getSemantic().getErrors()) {
//            System.out.println(error.toString());
//        }
//        System.out.println(root.getContents().size());
//        System.out.println("-------");
//        System.out.println(root.getChildren().get(0).getSymbolTableType().toString());
//        printSymbolTable(root,0);
        System.out.println(semantic.getErrors().size());
        printError(semantic.getErrors());
//        printAttribute(root);
        OutputGenerator.writeFiles(parser.getRoot(), parser.getErrors());
    }

    public static void printSymbolTable(SymbolTable root,int height){
        for (int i = 0; i < height; i++) {
            System.out.print("-");
        }
        System.out.println(root.getSymbolTableType() + " " + root.getName());
        for (SymbolTable child : root.getChildren()) {
            printSymbolTable(child,height + 1);
        }
    }

    public static void printAttribute(SymbolTable root){
        System.out.println(root.getSymbolTableType() + " " + root.getName());
        for (Attribute content : root.getContents()) {
            System.out.println("   name " + content.getName() + "  type is   " + content.getAttributeType() + "  address is " + content.getAddress() );

        }
        for (SymbolTable child : root.getChildren()) {
            printAttribute(child);
        }
    }

    public static void printError(LinkedList<Error> errors){
        for (Error error : errors) {
            System.out.println(error.toString());
        }
    }

}
