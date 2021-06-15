package top.wuare.json.lexer;

/**
 * JSON Token
 *
 * @author wuare
 * @date 2021/6/15
 */
public class Token {

    private int type;
    private byte[] val;

    public Token() {
    }

    public Token(int type, byte[] val) {
        this.type = type;
        this.val = val;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getVal() {
        return val;
    }

    public void setVal(byte[] val) {
        this.val = val;
    }
}
