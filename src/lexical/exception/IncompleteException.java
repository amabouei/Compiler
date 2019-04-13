package lexical.exception;

public class IncompleteException  extends LexicalException {

    public IncompleteException(String str) {
        this.str = str;
    }

    public IncompleteException(String str, int line) {
        this.str = str;
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return super.toString() + "incomplete)";
    }
}
