package top.wuare.calc;

/**
 * token
 *
 * @author wuare
 * @since 2021/7/7
 */
public class Token {

    public static final int EOF = -1;
    public static final int INTEGER = 1;
    public static final int PLUS = 2;
    public static final int MINUS = 3;
    public static final int MUL = 4;
    public static final int DIV = 5;
    public static final int LPAREN = 6;
    public static final int RPAREN = 7;

    private final int type;
    private final String value;

    public Token(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
