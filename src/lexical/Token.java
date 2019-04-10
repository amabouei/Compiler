package lexical;

public class Token {

    private String token;
    private TokenType tokenType;
    private int line;

    public Token(String token, TokenType tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    public Token(String token, TokenType tokenType, int line) {
        this.token = token;
        this.tokenType = tokenType;
        this.line = line;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "(" + tokenType.toString() + ", " + token + ")";
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getToken() {
        return token;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
}
