package top.wuare.json.lexer;

/**
 * JSON Token
 *
 * @author wuare
 * @date 2021/6/15
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

    public Token() {
    }

    public Token(int type, String val) {
        this.type = type;
        this.val = val;
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

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", val='" + val + '\'' +
                '}';
    }
}
