package outputGenerator;

import icg.TAC;
import parser.error.Error;
import parser.error.ErrorType;
import parser.treeStructure.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Map;

public class OutputGenerator {

    private static final String parserFileAddress = Paths.get(System.getProperty("user.dir"), "output_files", "parser.txt").toString();
    private static final String errorFileAddress = Paths.get(System.getProperty("user.dir"), "output_files", "errors.txt").toString();
    private static final String tacFileAddress =  Paths.get(System.getProperty("user.dir"), "Tester", "output.txt").toString();
    private static final String semanticErrorsFileAddress =  Paths.get(System.getProperty("user.dir"), "output_files", "semantic_errors.txt").toString();
    private static FileWriter tacFileWriter;
    private static FileWriter semanticErrorsFileWriter;
    private static FileWriter parserFileWriter;
    private static FileWriter errorFileWriter;

    public static void writeFiles(Node root, LinkedList<Error> errors, Map<Integer, TAC> programBlock, LinkedList<semantic.error.Error> semanticErrors) {
        try {
            parserFileWriter = new FileWriter(parserFileAddress);
            errorFileWriter = new FileWriter(errorFileAddress);
            tacFileWriter = new FileWriter(tacFileAddress);
            semanticErrorsFileWriter = new FileWriter(semanticErrorsFileAddress);
            if(semanticErrors.size() < 0)
                 printThreeAddressCodes(programBlock);
            printParseAndLexicalErrors(errors);
            printTree(root);
            printSemanticErros(semanticErrors);
            tacFileWriter.close();
            semanticErrorsFileWriter.close();
            parserFileWriter.close();
            errorFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printParseAndLexicalErrors(LinkedList<Error> errors) {
        for (int i = 0; i < errors.size();) {
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
            int curLine;
            if (!lexicalError.isEmpty()) {
                curLine = lexicalError.getFirst().getLine();
                printLexicalErrors(lexicalError, curLine);
            }
            if (!syntaxError.isEmpty()) {
                curLine = syntaxError.getFirst().getLine();
                printSyntaxErrors(syntaxError, curLine);
            }
        }
    }

    private static void printThreeAddressCodes(Map<Integer, TAC> programBlock) {
        try {
            for (int i = 0; i < programBlock.size(); i++)
                tacFileWriter.write(i + "\t" + programBlock.get(i).toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printSyntaxErrors(LinkedList<Error> syntaxErrors, int curLine) {
        try {
            errorFileWriter.write(curLine + "- Syntax Error! ");
            for (Error syntaxError : syntaxErrors) {
                errorFileWriter.write(syntaxError.toString() + " ");
            }
            errorFileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printLexicalErrors(LinkedList<Error> lexicalErrors, int curLine) {
        try {
            errorFileWriter.write(curLine + "- Lexical Error! ");
            for (Error error : lexicalErrors) {
                errorFileWriter.write(error.toString() + " ");
            }
            errorFileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String printError(Error error, int prevLine) {
        if (prevLine == error.getLine())
            return " " + error.toString();
        else if (prevLine != 0)
            return "\n" + error.getLine() + "- " + error.toString();
        else
            return error.getLine() + "- " + error.toString();
    }


    private static void printSemanticErros(LinkedList<semantic.error.Error> semanticErrors) {
        try {
            for (semantic.error.Error semanticError : semanticErrors) {
                semanticErrorsFileWriter.write(semanticError.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printTree(Node node) {
        try {
            for (int height = node.getHeight(); height > 0; height--) {
                parserFileWriter.write("|  ");
            }
            parserFileWriter.write(node.getLabel() + "\n");
            if (!node.isTerminal()) {
                for (Node child : node.getChildren()) {
                    printTree(child);
                }
            }
        }  catch (IOException e) {
            System.out.println("Parser file not found!");
            e.printStackTrace();
        }
    }
}
