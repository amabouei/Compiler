import icg.ICG;
import lexical.Creator;
import lexical.LexicalAnalyzer;
import outputGenerator.OutputGenerator;
import parser.Grammar;
import parser.Parser;
import semantic.AddressGenerator;
import semantic.Semantic;
import semantic.SymbolTable;
import semantic.SymbolTableType;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = Creator.initializeLexical();
        RandomAccessFile codeInput = null;
        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();
        try {
            codeInput = new RandomAccessFile(Paths.get(System.getProperty("user.dir"), "inputs", file + ".txt").toString(), "r");
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
        OutputGenerator.writeFiles(parser.getRoot(), parser.getErrors(), icg.getProgramBlock(), semantic.getErrors());
    }

}
