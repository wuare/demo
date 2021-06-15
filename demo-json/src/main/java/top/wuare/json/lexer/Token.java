package top.wuare.json.lexer;

/**
 * JSON Token
 *
 * @author wuare
 * @date 2021/6/15
 */
public class Token {

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
