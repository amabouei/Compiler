package lexical;

import java.util.regex.Pattern;

public enum InputType {
    DIGIT("[0-9]"), LETTER("[A-Za-z]"), STAR("[\\*]"),
    SYMBOL("[<\\-+\\[\\]{}(),:;]"), EQUALS("[=]"),
    SLASH("[/]"),
    END_LINE("[\n]"), WHITESPACE("[\\s\\v\t\r\f]"),
    EOF("EOF"), OTHER("");

    private final String regex;

    InputType(String input) {
        this.regex = input;
    }

    public static InputType getTypeByChar(char inputChar) {

        for (InputType type : InputType.values()) {
            if (Pattern.matches(type.regex, String.valueOf(inputChar))) {
                return type;
            }
        }
        return OTHER;
    }

}
