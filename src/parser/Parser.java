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
                    Token next = parse(child, grammar.getSubDiagrams().get(edge.getLabel()),curToken);
                    curToken = next;
                }else{
                    if(!edge.getLabel().equals("epsilon")){
                        curToken = null;
                    }
                }
                curState = edge.getNext();
                current.getChilds().add(child);
            }else{
                System.out.println("error " + curDiagram.getName() + "  " + curToken.getTokenType() +"   "+curToken.getToken());
                break;
            }
        }
        return null;
    }


    public Edge nextStateChooser(ArrayList<Edge> edges, Token input,String curDiagName){
        String str = input.getToken();
        if(input.getTokenType() == TokenType.NUM || input.getTokenType() == TokenType.ID || input.getTokenType() == TokenType.EOF){
            str = input.getTokenType().toString().toLowerCase();
        }
        for (Edge edge : edges) {
            if(edge.getLabel().equals("epsilon")){
                Set<String> followOfState = grammar.getFollowSets().get(curDiagName);
                if(followOfState.contains(str)){
                    return edge;
                }
            }
            if(edge.isToken()){
                String label = edge.getLabel();
                if(label.toUpperCase().equals(input.getTokenType().toString()) || label.equals(str)){ //// if token is (id or num) or another things
                    return edge;
                }
            }else {
                Set<String> firstOfState = grammar.getFirstSets().get(edge.getLabel());
                if(firstOfState.contains(str) || (firstOfState.contains("epsilon") && grammar.getFollowSets().get(edge.getLabel()).contains(str))){
                    return edge;
                }
            }
        }
        return null;
    }
}
