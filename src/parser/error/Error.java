package parser.error;

public class Error {
   private int line;
   private String str;
   private ErrorType errorType;

   public Error(int line, String str, ErrorType errorType) {
        this.line = line;
        this.str = str;
        this.errorType = errorType;
   }

    public int getLine() {
        return line;
    }

    public String getStr() {
        return str;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String toString() {
       String toString = str;
       if (errorType != ErrorType.Lexical)
           toString = "(" + errorType.toString() + " " + str + ")";
       return toString;
    }
}
