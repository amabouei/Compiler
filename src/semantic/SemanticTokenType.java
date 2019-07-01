package semantic;

public enum SemanticTokenType {
    DEFMAIN,CREATEVAR,INITIAL_VAR,NOTVOID,INITIALARRAY,INT,VOID,CREATEFUNCTION,PUSH,INT_CREATEVAR
    ,POP2,INITIALPOINTER,NEWSCOPE,ENDOFSCOPE,CONTINUE,BREAK,WHILE,SWITCH,FINDVAR,NUM,
    BEGINARGS,IDISNUMBERIC,ENDARGS,ARG,EXPERSSIONISNUMBERIC,EXPERSSIONRESET;


    public static SemanticTokenType getSemanticToken(String name) {
        for (SemanticTokenType token : SemanticTokenType.values()) {
                return token;
        }
        return null;
    }
}
