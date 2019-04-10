package lexical_step;

import java.util.StringJoiner;
import java.util.regex.Pattern;

public enum InputType {


    //EOF and whitespace...

    DIGIT("[0-9]"),LETTER("[A-Za-z]"),STAR("[*]"),
    SYMBOL("[<*-+}\\[\\]{)(,:;]"),EQUAL("[=]"),
    SLASH("[/]") , BACKSLASH("[\\\\]"),
    ENDLINE("\n"),WHITESPACE("[\t\r\f]"),
    EOF("asdfs") , OTHER("");

    private final String regex;


    InputType(String input) {
        this.regex = input;
    }
    //TODO
    public static  InputType getTypeBychar(char inputChar){

        for (InputType type : InputType.values()) {
            if(Pattern.matches(type.regex, String.valueOf(inputChar))){
                return type;
            }
        }
        return OTHER;
    }

}
