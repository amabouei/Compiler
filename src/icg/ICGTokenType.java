package icg;

public enum ICGTokenType {

    MAIN,INITIAL_MAIN,AND_INITIAL_OUTPUTJUMPER,POP,JP_FIRST,JP_END,IF_JP,IF_SAVE_JP,IF_SAVE,WHILE_JP,WHILE_END
    ,RETURN,PUSH_NULL,SWITCH_VAR,SWITCH_EXTRA_JP,SWITH_END,SWITCH_SAVE,SWITCH_SAVE_JP,PID,CALING_FUNCTION,ASSING_PUSH,PUSH_ZERO,PUSH_RELOP,ADDOP,MULT,NOT,
    PUSH_NUM,INDEXING,ASSING_TOFUNC,OUTPUT_JUMPER_ASSIGN,END_OF_FILE,ASSIGN_FIRST_LINE,PUSH,JUMPER;


    public static ICGTokenType getTokenByName (String name) {
        for (ICGTokenType token : ICGTokenType.values()) {
            if (token.name().equals(name.toUpperCase()))
                return token;
        }
        return null;
    }
}
