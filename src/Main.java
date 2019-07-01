import icg.ICG;
import lexical.Creator;
import lexical.LexicalAnalyzer;
import outputGenerator.OutputGenerator;
import parser.Grammar;
import parser.Parser;
import semantic.AddressGenerator;
import semantic.Semantic;
import semantic.SymbolTable;
import semantic.error.Error;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;

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
        SymbolTable root = new SymbolTable();
        AddressGenerator addressGenerator = new AddressGenerator();
        ICG icg = new ICG(addressGenerator);
        Semantic semantic = new Semantic(root, addressGenerator);
        Parser parser = new Parser(new Grammar(), lexicalAnalyzer, semantic, icg);
        parser.parseTree();

        //TODO file...

        for (Error error : parser.getSemantic().getErrors()) {
            System.out.println(error.toString());
        }

        printSymbolTable(root);
        OutputGenerator.writeFiles(parser.getRoot(), parser.getErrors());
    }

    public static void printSymbolTable(SymbolTable root){
        System.out.println(root.getSymbolTableType());
        for (SymbolTable child : root.getChildren()) {
            printSymbolTable(child);
        }
    }

}
