package lexical_step;

public class State {

    private int id;
    private int lineNumber;
    private String str = null;
    private boolean isFinal;
    private boolean needBack;
    private TokenType tokenType = null;

    public State(int id, boolean isFinal, boolean needBack) {
        this.id = id;
        this.isFinal = isFinal;
        this.needBack = needBack;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getId() {
        return id;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getStr() {
        return str;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public boolean isNeedBack() {
        return needBack;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
