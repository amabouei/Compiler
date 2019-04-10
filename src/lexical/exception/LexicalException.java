package lexical.exception;

public class LexicalException extends Exception {
    protected String str;
    protected int line;

    public String getStr() {
        return str;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "(" + str + ", ";
    }
}
