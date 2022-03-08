package top.wuare.json.lexer;

public enum TokenType {

    LBRACE("{"),
    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]"),
    COLON(":"),
    COMMA(","),
    STRING("字符串"),
    NUMBER("数字"),
    LITERAL_TRUE("true"),
    LITERAL_FALSE("false"),
    LITERAL_NULL("null");

    private final String text;

    TokenType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
