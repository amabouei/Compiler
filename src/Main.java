import lexical.Creator;
import lexical.LexicalAnalyzer;
import outputGenerator.OutputGenerator;
import parser.Grammar;
import parser.Parser;
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
        Parser parser = new Parser(new Grammar(), lexicalAnalyzer);
        parser.parseTree();
        OutputGenerator.writeFiles(parser.getRoot(), parser.getErrors());
    }
}
