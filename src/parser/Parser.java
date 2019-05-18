package parser;


import lexical.LexicalAnalyzer;
import lexical.Token;
import lexical.TokenType;
import lexical.exception.IncompleteException;
import lexical.exception.InvalidInputException;
import parser.error.Error;
import parser.error.ErrorType;
import parser.treeStructure.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

public class Parser {

    private Grammar grammar;
    private Node root;
    private LexicalAnalyzer lexicalAnalyzer;
    private LinkedList<Error> errors = new LinkedList<>();
    private int curLine = 1;

    public Parser(Grammar grammar, LexicalAnalyzer lexicalAnalyzer) {
        this.grammar = grammar;
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.root = new Node("Program", null, false, 0);
    }

    public Node parseTree() {
        parse(root, grammar.getSubDiagrams().get("Program"), null);
        return root;
    }

    public Token parse(Node current, Diagram curDiagram, Token curToken) {
        State curState = curDiagram.getStart();
        int nextLine = 1;
        if (curToken != null)
            nextLine = curToken.getLine();
        while (true) {
            if (curState.isFinal()) {
                return curToken;
            }
            if (curToken == null) {
                try {
                    while (true) {
                        curToken = lexicalAnalyzer.getNextToken();
                        nextLine = curToken.getLine();//TODO
                        if (!(curToken.getTokenType() == TokenType.COMMENT || curToken.getTokenType() == TokenType.WHITESPACE)) {
                            break;
                        }
                    }
                } catch (IncompleteException | InvalidInputException e) {
                    errors.add(new Error(e.getLine(), e.toString(), ErrorType.Lexical));
                }
            }
            Edge edge = nextStateChooser(curState.getEdges(), curToken, curDiagram.getName());
            if (edge != null) {
                Node child = new Node(edge.getLabel(), current, edge.isToken(), current.getHeight() + 1);
//                if (edge.getLabel().equals("id") || edge.getLabel().equals("num")) {
//                    child.setLabel(curToken.getToken());
//                }
                if (!edge.isToken()) {
                    Token next = parse(child, grammar.getSubDiagrams().get(edge.getLabel()), curToken);
                    curToken = next;
                } else {
                    if (!edge.getLabel().equals("epsilon")) {
                        curToken = null;
                    }
                }
                curState = edge.getNext();
                current.getChildren().add(child);

            } else {
                if (curToken.getTokenType() == TokenType.EOF) {
                    break;
                    //todo
                } else {
                    Edge expectedEdge = curState.getEdge();
                    System.out.println(curState.getEdge().getLabel());
                    if (expectedEdge.isToken()) {

                        errors.add(new Error(curLine, expectedEdge.getLabel(), ErrorType.Missing));
                        curState = expectedEdge.getNext();
                    } else {

                        Set<String> firstOfState = grammar.getFirstSets().get(expectedEdge.getLabel());
                        if (!firstOfState.contains("epsilon") && grammar.getFollowSets().get(expectedEdge.getLabel()).contains(curToken.getToken())) {
                            errors.add(new Error(curLine, expectedEdge.getLabel(), ErrorType.Missing));
                            curState = expectedEdge.getNext();
                        } else {
                            errors.add(new Error(curLine, curState.getEdge().getLabel(), ErrorType.Unexpected));
                            curToken = null;
                        }
                    }
                }
            }
            if (curToken == null) {
                curLine = nextLine;
            }
        }
        return null;
    }


    public Edge nextStateChooser(ArrayList<Edge> edges, Token input, String curDiagName) {
        String str = input.getToken();
        if (input.getTokenType() == TokenType.NUM || input.getTokenType() == TokenType.ID || input.getTokenType() == TokenType.EOF) {
            str = input.getTokenType().toString().toLowerCase();
        }
        for (Edge edge : edges) {
            if (edge.getLabel().equals("epsilon")) {
                Set<String> followOfState = grammar.getFollowSets().get(curDiagName);
                if (followOfState.contains(str)) {
                    return edge;
                }
            }
            if (edge.isToken()) {
                String label = edge.getLabel();
                if (label.toUpperCase().equals(input.getTokenType().toString()) || label.equals(str)) { //// if token is (id or num) or another things
                    return edge;
                }
            } else {
                Set<String> firstOfState = grammar.getFirstSets().get(edge.getLabel());
                if (firstOfState.contains(str) || (firstOfState.contains("epsilon") && grammar.getFollowSets().get(edge.getLabel()).contains(str))) {
                    return edge;
                }
            }
        }
        return null;
    }

    public LinkedList<Error> getErrors() {
        return errors;
    }
}
