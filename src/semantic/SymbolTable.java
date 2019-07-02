package semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class SymbolTable {

    private SymbolTable parent = null;
    private LinkedList<SymbolTable> children = new LinkedList<>();
//    private HashMap<String ,Symbol> symbols; ///mohem nist !
    private LinkedList<Attribute> contents = new LinkedList<>();
    private String name;
    private SymbolTableType symbolTableType;
    private int startLine;
    public SymbolTable() {

    }


    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
    }

    public SymbolTable(SymbolTable parent, String name, SymbolTableType symbolTableType) {
        this.parent = parent;
        this.name = name;
        this.symbolTableType = symbolTableType;
    }

    public SymbolTable(SymbolTable parent, SymbolTableType symbolTableType) {
        this.parent = parent;
        this.symbolTableType = symbolTableType;
    }

    public Attribute findInSelfOrParent(String symbolName){
        Attribute  output = null;
        output = this.contains(symbolName);
        if(output != null){
            return output;
        }
        if(parent != null && parent.symbolTableType == SymbolTableType.FUNCTION) {
            return parent.contains(symbolName);
        }
        return null;
    }

    public Attribute find(String symbolName){
        Attribute  output = null;
        output = this.contains(symbolName);
        if(output != null){
                return output;
        }
        if(parent != null) {
            return parent.find(symbolName);
        }
        return null;
    }

    public Attribute contains (String symbolName) {
        for (Attribute attribute : contents) {
            if ((attribute.getName() != null)) {
                if (attribute.getName().equals(symbolName))
                    return attribute;
            }
        }
        return null;
    }

    public void defineNewScope (SymbolTable newScope) {
        children.add(newScope);
    }

    public void defineNewAttribute(Attribute attribute){
//        if(this.contains(attribute.getName()) != null){
//            return;
//        }
        //todo !!!? what is it
        contents.add(attribute);
    }

    public LinkedList<SymbolTable> getChildren() {
        return children;
    }

    public LinkedList<Attribute> getContents() {
        return contents;
    }

    public SymbolTable getParent() {
        return parent;
    }


    public SymbolTable getFunction(String name){
        for (SymbolTable child : children) {
            if(child.getSymbolTableType() == SymbolTableType.FUNCTION &&  child.getName() != null && child.getName().equals(name)){
                return child;
            }
        }
        if(parent != null){
            return parent.getFunction(name);
        }
        return null;
    }

    public SymbolTable getFunctionInScope(String name){
        for (SymbolTable child : children) {
            if(child.getSymbolTableType() == SymbolTableType.FUNCTION &&  child.getName() != null && child.getName().equals(name)){
                return child;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public SymbolTableType getSymbolTableType() {
        return symbolTableType;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getStartLine() {
        return startLine;
    }

    public boolean isExistAppropriateBlockForBreak(){

        if(symbolTableType.equals(SymbolTableType.WHILE) || symbolTableType.equals(SymbolTableType.SWITCH)){
            return true;
        }else{
            if(parent != null && parent.symbolTableType != SymbolTableType.FUNCTION){
                return parent.isExistAppropriateBlockForBreak();
            }
        }
        return false;
    }

    public boolean isExistAppropriateBlockForContinue(){

        if(symbolTableType.equals(SymbolTableType.WHILE)){
            return true;
        }else{
            if(parent != null && parent.symbolTableType != SymbolTableType.FUNCTION){
                return parent.isExistAppropriateBlockForContinue();
            }
        }
        return false;
    }

    public SymbolTable findFatherFunction(){
        if(this.getSymbolTableType() == SymbolTableType.FUNCTION){
            return this;
        }
        if(parent != null ){
            return parent.findFatherFunction();
        }
        return null;
    }
}
