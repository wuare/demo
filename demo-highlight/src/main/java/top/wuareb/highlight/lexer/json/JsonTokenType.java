package top.wuareb.highlight.lexer.json;

public enum JsonTokenType {

    STRING("字符串"),
    NUMBER("数字"),
    ARRAY("数组"),
    OBJECT("对象"),
    TRUE("true"),
    FALSE("false"),
    NULL("null"),
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
