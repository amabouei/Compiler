package lexical.exception;

public class InvalidInputException extends LexicalException {

    public InvalidInputException(String str,int line){
        this.str = str;
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return super.toString()  + "invalid input )";
    }
}
