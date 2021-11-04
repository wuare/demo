package top.wuare.express;

public class ExpToken {

    private ExpTokenType type;
    private String text;
    private int line;
    private int column;

    public ExpToken(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public ExpToken(ExpTokenType type, String text, int line, int column) {
        this.type = type;
        this.text = text;
        this.line = line;
        this.column = column;
    }

    public ExpTokenType getType() {
        return type;
    }

    public void setType(ExpTokenType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        return "ExpToken{" +
                "type=" + type +
                ", text='" + text + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
