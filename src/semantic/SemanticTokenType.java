package semantic;

public enum SemanticTokenType {
    NOT_VOID, CREATE_VAR, ASSIGN_NAME, INT, FIND_ID, VOID, IS_NUMERIC, ID_IS_NUMERIC, RETURN_ADDRESS, NUMERIC,
    NEW_SCOPE, END_OF_SCOPE, BREAK, CONTINUE,
    WHILE, END_OF_WHILE, SWITCH, END_OF_SWITCH, BEGIN_ARGS, END_ARGS, ARG, NUM, INITIAL_VAR, INT_CREATEVAR,
    INITIALARRAY, INITIALPOINTER, PUSH, POP2, CREATEFUNCTION, DEFMAIN;


    public static SemanticTokenType getSemanticToken(String name) {
        for (SemanticTokenType token : SemanticTokenType.values()) {
            if (token.name().replaceAll("_", "").equals(name.toUpperCase()))
                return token;
        }
        return null;
    }
}
