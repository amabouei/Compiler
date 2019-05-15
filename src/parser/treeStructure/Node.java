package parser.treeStructure;

import java.util.LinkedList;

public class Node {
    private String label;
    private Node parent;
    private boolean terminal;
    private int height;
    private LinkedList<Node> children = new LinkedList<>();

    public Node(String label, Node parent, boolean terminal, int height) {
        this.label = label;
        this.parent = parent;
        this.terminal = terminal;
        this.height = height;
    }

    public String getLabel() {
        return label;
    }

    public Node getParent() {
        return parent;
    }

    public LinkedList<Node> getChildren() {
        return children;
    }

    public int getHeight() {
        return height;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
