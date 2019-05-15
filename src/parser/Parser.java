package parser;


import lexical.LexicalAnalyzer;
import lexical.Token;
import lexical.TokenType;
import lexical.exception.IncompleteException;
import lexical.exception.InvalidInputException;
import parser.treeStructure.Node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

public class Parser {

    private Grammar grammar;
    private Node root;
    private LexicalAnalyzer lexicalAnalyzer;


    public Parser(Grammar grammar, LexicalAnalyzer lexicalAnalyzer) {
        this.grammar = grammar;
        this.lexicalAnalyzer = lexicalAnalyzer;
        this.root = new Node("Program",null,false,0);
    }

    public Node parseTree(){
        parse(root,grammar.getSubDiagrams().get("Program"),null);
        return root;
    }

    public Token parse(Node current,Diagram curDiagram,Token curToken ){
        State curState = curDiagram.getStart();
        while(true) {
            if (curState.isFinal()) {
                return curToken;
            }
            if(curToken == null) {
                try {
                    while(true) {
                        curToken = lexicalAnalyzer.getNextToken();
                        if (!(curToken.getTokenType() == TokenType.COMMENT || curToken.getTokenType() == TokenType.WHITESPACE)) {
                            break;
                        }
                    }
                } catch (IncompleteException e) {
                    e.printStackTrace();
                } catch (InvalidInputException e) {
                    e.printStackTrace();
                }
            }
            Edge edge = nextStateChooser(curState.getEdges(), curToken,curDiagram.getName());
            if (edge != null) {

                Node child = new Node(edge.getLabel(), current,edge.isToken(),current.getHeight() + 1);
                if (!edge.isToken()) {
                    curToken = parse(child, grammar.getSubDiagrams().get(edge.getLabel()),curToken);
                }else{
                    if(!edge.getLabel().equals("epsilon")){
                        curToken = null;
                    }
                }
                curState = edge.getNext();
                current.getChilds().add(child);
            }else{
                break;
            }
        }
        return null;
    }


    public Edge nextStateChooser(ArrayList<Edge> edges, Token input,String curDiagName){

        for (Edge edge : edges) {
            if(edge.getLabel().equals("epsilon")){
                Set<String> followOfState = grammar.getFollowSets().get(curDiagName);
                if(followOfState.contains(input.getToken())){
                    return edge;
                }
            }
            if(edge.isToken() ){
                String label = edge.getLabel();
                if(label.toUpperCase().equals(input.getTokenType().toString()) || label.equals(input.getToken())){ //// if token is (id or num) or another things
                    return edge;
                }
            }else {
                Set<String> firstOfState = grammar.getFirstSets().get(edge.getLabel());
                if(firstOfState.contains(input.getToken()) || (firstOfState.contains("epsilon") && grammar.getFollowSets().get(edge.getLabel()).contains(input.getToken()))){
                    return edge;
                }
            }
        }
        return null;
    }
}
