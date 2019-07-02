package icg;

public enum ICGTokenType {

    MAIN,END_OF_FILE,ASSIGN_FIRST_LINE,POP,JP_FIRST,JP_END,IF_JP,
    IF_SAVE_JP,IF_SAVE,WHILE_START,WHILE_JP,WHILE_END,
    RETURN_FUNC,PUSH_NULL,SWITCH_START,SWITCH_EXTRA_JP,SWITH_END,
    SWITCH_SAVE,SWITCH_SAVE_JP,JUMPER,PID,INDEXING,CALING_FUNCTION,
    ASSIGN_PUSH,PUSH_ZERO,PUSH_RELOP,CALCULATE,ADDOP,
    MULT,GHARINE,PUSH_NUM,START_ARG,ASSIGN_INPUT,ENDFUNCTION;

    public static ICGTokenType getTokenByName (String name) {

        for (ICGTokenType token : ICGTokenType.values()) {
            if (token.name().equals(name.toUpperCase()))
                return token;
        }
        return null;
    }
}
