package parser.error;

import java.util.Comparator;

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

//
//    @Override
//    public int compare(Object o1, Object o2) {
//
//    }
}
