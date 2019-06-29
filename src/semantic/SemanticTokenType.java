package semantic;

public enum SemanticTokenType {
    NOT_VOID, CREATE_VAR, ASSIGN_NAME, INT, FIND_ID, VOID, IS_NUMERIC, ID_IS_NUMERIC, NUMERIC, NEW_SCOPE, END_OF_SCOPE, BREAK, CONTINUE,
    WHILE, END_OF_WHILE, SWITCH, END_OF_SWITCH, BEGIN_ARGS, END_ARGS, ARG, NUM;

    public static SemanticTokenType getSemanticToken (String name) {
        for (SemanticTokenType token : SemanticTokenType.values()) {
            if (token.name().equals(name.toUpperCase()))
                return token;
        }
        return null;
    }
}