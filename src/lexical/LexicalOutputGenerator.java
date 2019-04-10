package lexical;

import lexical.exception.LexicalException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;

public class LexicalOutputGenerator {
    private static FileWriter fileWriter;


    public static void writeFiles(LinkedList<Token> tokens, LinkedList<LexicalException> errors) {
        try {
            printTokens(tokens);
            printErrors(errors);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printTokens(LinkedList<Token> tokens) {
        try {
            fileWriter = new FileWriter(Paths.get(System.getProperty("user.dir"), "lexical_outputs", "scanner.txt").toString());
            int lineNumber = 0;
            for (Token token : tokens) {
                try {
                    fileWriter.write(getLineToPrint(++lineNumber, token));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printErrors(LinkedList<LexicalException> errors) {
        try {
            fileWriter = new FileWriter(Paths.get(System.getProperty("user.dir"), "lexical_outputs", "lexical_errors.txt").toString());
            int lineNumber = 0;
            for (LexicalException error : errors) {
                try {
                    fileWriter.write(getLineToPrint(++lineNumber, error));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static String getLineToPrint(int lineNumber, Token token) {
        return lineNumber + "- " + token.toString() + "\n";
    }

    private static String getLineToPrint(int lineNumber, LexicalException error) {
        return lineNumber + "- " + error.toString() + "\n";
    }
}
