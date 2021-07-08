package top.wuare.calc;

/**
 * token
 *
 * @author wuare
 * @date 2021/7/7
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

    private int type;
    private String value;

    public Token(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
