package top.wuare.json.lexer;

/**
 * JSON Token
 *
 * @author wuare
 * @since 2021/6/15
 */
public class Token {
    // {
    public static final int LBRACE = 1;
    public static final int RBRACE = 2;
    // [
    public static final int LBRACKET = 3;
    public static final int RBRACKET = 4;
    // :
    public static final int COLON = 5;
    public static final int COMMA = 6;
    public static final int STRING = 7;
    public static final int NUMBER = 8;
    public static final int LITERAL_TRUE = 9;
    public static final int LITERAL_FALSE = 10;
    public static final int LITERAL_NULL = 11;

    private int type;
    private String val;
    private int line;
    private int column;

    public Token() {
    }

    public Token(int type, String val) {
        this.type = type;
        this.val = val;
    }

    public Token(int type, String val, int line, int column) {
        this.type = type;
        this.val = val;
        this.line = line;
        this.column = column;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
