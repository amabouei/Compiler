package semantic;

public enum SemanticTokenType {
    DEFMAIN, CREATEVAR, INITIAL_VAR, NOTVOID, INITIALARRAY, INT, VOID, CREATEFUNCTION, PUSH, INT_CREATEVAR, POP2,
    INITIALPOINTER, NEWSCOPE, ENDOFSCOPE, CONTINUE, BREAK, WHILE, SWITCH, FINDVAR, NUM, BEGINARGSISNUMERIC,
    BEGINARGS, IDISNUMBERIC, ENDARGS, ARG, EXPRESSIONISNUMERIC, EXPRESSIONRESET, BACK,RETURNCHECKER,PUSH_LINE;


    public static SemanticTokenType getSemanticToken(String name) {
        for (SemanticTokenType token : SemanticTokenType.values()) {
            if (token.name().equals(name.toUpperCase()))
                return token;
        }
        return null;
    }
}
