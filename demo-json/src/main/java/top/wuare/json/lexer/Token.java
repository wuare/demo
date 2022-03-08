package top.wuare.json.lexer;

/**
 * JSON Token
 *
 * @author wuare
 * @since 2021/6/15
 */
public class Token {

    private TokenType type;
    private String val;
    private int line;
    private int column;

    public Token() {
    }

    public Token(TokenType type, String val) {
        this.type = type;
        this.val = val;
    }

    public Token(TokenType type, String val, int line, int column) {
        this.type = type;
        this.val = val;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", val='" + val + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
