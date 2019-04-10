package lexical.exception;

import java.security.spec.ECFieldF2m;

public class IncompleteException  extends LexicalException {

    public IncompleteException() {

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
