package semantic.error;

public class Error {
    private int lineNumber;
    private ErrorType type;
    private String id;

    public Error(int lineNumber, ErrorType type, String id) {
        this.lineNumber = lineNumber;
        this.type = type;
        this.id = id;
    }

    public Error(int lineNumber, ErrorType type) {
        this.lineNumber = lineNumber;
        this.type = type;
    }

    public Error(ErrorType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String toString = lineNumber + "-\t";
        switch (type) {
            case ID_NOT_DEFINED:
                toString = toString + "'" + id + "' is not defined";
                break;
            case ILLEGAL_TYPE_OF_VOID:
                toString += "Illegal type of void";
                break;
            case MISMATCHED_NUMBER_OF_ARGUMENTS:
                toString += "Mismatch number of arguments of '" + id + "'";
                break;
            case NO_WHILE_FOR_CONTINUE:
                toString += "No 'while' found for 'continue'";
                break;
            case NO_WHILE_OR_SWITCH_FOR_BREAK:
                toString += "No 'while' or 'switch' found for 'break'";
                break;
            case TYPE_MISMATCH:
                toString += "Type mismatch in operands";
                break;
            case ID_ALREADY_DEFINED:
                toString += "'" + id + "' is already defined";
                break;
                default:
                    toString += "WHAT IS THE ERROR";
                    break;
        }
        if (type != ErrorType.MAIN_NOT_FOUND)
            return toString + ".";
        else
            return "Main method not found!";
    }
}
