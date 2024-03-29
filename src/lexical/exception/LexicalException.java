package lexical.exception;

public class LexicalException extends Exception {
    protected String str;
    protected int line;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "(" + str + ", ";
    }
}
