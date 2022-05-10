package top.wuareb.highlight.lexer.json;

public enum JsonTokenType {

    RBRACE("}"),
    LBRACKET("["),
    RBRACKET("]"),
    COLON(":"),
    COMMA(","),
    STRING("字符串"),
    NUMBER("数字"),
    LBRACE("{"),
    TRUE("true"),
    FALSE("false"),
    NULL("null"),

    WHITE_SPACE("空白"),
    TEXT("其它字符"),
    EOF("结束标识")
    ;

    private final String desc;

    JsonTokenType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
