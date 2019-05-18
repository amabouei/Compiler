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
    private boolean completeParse = false;

    public Parser(Grammar grammar, LexicalAnalyzer lexicalAnalyzer) {
        this.grammar = grammar;
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.root = new Node("Program", null, false, 0);
    }

    public void parseTree() {
        parse(root, grammar.getSubDiagrams().get("Program"), null);
    }

    public Node getRoot() {
        return root;
    }

    public Token parse(Node current, Diagram curDiagram, Token curToken) {
        State curState = curDiagram.getStart();
        while (!completeParse) {
            if (curState.isFinal())
                return curToken;
            if (curToken == null)
                curToken = getNextToken();
            Edge edge = nextStateChooser(curState.getEdges(), curToken, curDiagram.getName());
            if (edge != null) {
                Node child = new Node(edge.getLabel(), current, edge.isToken(), current.getHeight() + 1);
                if (edge.getLabel().equals("id") || edge.getLabel().equals("num")) {
                    child.setLabel(curToken.getToken());
                } // if wanting token name
                curState = edge.getNext();
                current.getChildren().add(child);
                if (!edge.isToken()) {
                    Token next = parse(child, grammar.getSubDiagrams().get(edge.getLabel()), curToken);
                    curToken = next;
                } else if (!edge.getLabel().equals("epsilon") ){
                    curToken = null;
                }
            } else {
                Edge expectedEdge = curState.getEdge();
                State nextState = null;
                try {
                    nextState = errorHandling(expectedEdge, curToken);
                    if (nextState == null) {
                        curToken = null; // if stay in current state must get new token;
                    } else {
                        curState = nextState;
                    }
                } catch (Exception e) {
                    completeParse = true;
                    break;
                }
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

    public State errorHandling(Edge expectedEdge, Token curToken) throws Exception {
        int curLine = curToken.getLine();
        if (curToken.getTokenType() == TokenType.EOF) {
            errors.add(new Error(curLine, ErrorType.UnexpectedEndOfFile.toString(), ErrorType.UnexpectedEndOfFile));
            throw new Exception();
        } else {

            if (expectedEdge.isToken()) {
                if (expectedEdge.getLabel() == "eof") {
                    errors.add(new Error(curLine, expectedEdge.getLabel(), ErrorType.MalformedInput));// see doc
                    throw new Exception();
                } else {
                    errors.add(new Error(curLine, expectedEdge.getLabel(), ErrorType.Missing));
                    return expectedEdge.getNext();
                }
            } else {
                Set<String> firstOfState = grammar.getFirstSets().get(expectedEdge.getLabel());
                if (!firstOfState.contains("epsilon") && grammar.getFollowSets().get(expectedEdge.getLabel()).contains(curToken.getToken())) {
                    errors.add(new Error(curLine, expectedEdge.getLabel(), ErrorType.Missing));
                    return expectedEdge.getNext();
                } else {
                    errors.add(new Error(curLine, expectedEdge.getLabel(), ErrorType.Unexpected));//TODO description....
                }
            }
        }
        return null;

    }

    public Token getNextToken() {
        Token token;
        while (true) {
            try {
                token = lexicalAnalyzer.getNextToken();
                if (!(token.getTokenType() == TokenType.COMMENT || token.getTokenType() == TokenType.WHITESPACE)) {
                    break;
                }

            } catch (IncompleteException | InvalidInputException e) {
                errors.add(new Error(e.getLine(), e.toString(), ErrorType.Lexical));
            }
        }
        return token;

    }

    public LinkedList<Error> getErrors() {
        return errors;
    }
}
