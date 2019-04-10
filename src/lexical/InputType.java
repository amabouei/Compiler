package lexical;

import java.util.regex.Pattern;

public enum InputType {


    //TODO EOF and whitespace...

    DIGIT("[0-9]"), LETTER("[A-Za-z]"), STAR("[*]"),
    SYMBOL("[<*\\-+\\[\\]{}(),:;]"), EQUALS("[=]"),
    SLASH("[/]"),
    END_LINE("[\n]"), WHITESPACE("[\\s\\v\t\r\f]"),
    EOF("[asdf]"), OTHER("");

    private final String regex;


    InputType(String input) {
        this.regex = input;
    }

    //TODO
    public static InputType getTypeByChar(char inputChar) {

        for (InputType type : InputType.values()) {
            if (Pattern.matches(type.regex, String.valueOf(inputChar))) {
                return type;
            }
        }
        return OTHER;
    }

}
