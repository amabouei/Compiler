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
            fileWriter = new FileWriter(Paths.get(System.getProperty("user.dir"), "output_files", "scanner.txt").toString());
            int prevLine = 0;
            for (Token token : tokens) {
                try {
                    fileWriter.write(getLineToPrint(token, prevLine));
                    prevLine = token.getLine();
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
            fileWriter = new FileWriter(Paths.get(System.getProperty("user.dir"), "output_files", "lexical_errors.txt").toString());
            int prevLine = 0;
            for (LexicalException error : errors) {
                try {
                    fileWriter.write(getLineToPrint(error, prevLine));
                    prevLine = error.getLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static String getLineToPrint(Token token, int prevLine) {
        if (prevLine == token.getLine())
            return " " + token.toString();
        else if (prevLine != 0)
            return "\n" + token.getLine() + "- " + token.toString();
        else
            return token.getLine() + "- " + token.toString();
    }

    private static String getLineToPrint(LexicalException error, int prevLine) {
        if (prevLine == error.getLine())
            return " " + error.toString();
        else if (prevLine != 0)
            return "\n" + error.getLine() + "- " + error.toString();
        else
            return error.getLine() + "- " + error.toString();
    }
}
