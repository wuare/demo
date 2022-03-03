package top.wuareb.highlight.lexer.wa;

public class WaToken {
    private WaTokenType type;
    private String text;

    public WaToken() {
    }

    public WaToken(WaTokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public WaTokenType getType() {
        return type;
    }

    public void setType(WaTokenType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "WaToken{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}
