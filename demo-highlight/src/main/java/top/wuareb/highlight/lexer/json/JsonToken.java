package top.wuareb.highlight.lexer.json;

public class JsonToken {
    
    private JsonTokenType type;
    private String value;
    private int line;
    private int column;

    public JsonToken(JsonTokenType type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public JsonToken(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public JsonTokenType getType() {
        return type;
    }

    public void setType(JsonTokenType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
                ", value='" + value + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
