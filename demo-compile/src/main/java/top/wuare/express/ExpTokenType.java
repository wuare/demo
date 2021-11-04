package top.wuare.express;

public enum ExpTokenType {

    NUMBER("NUMBER", "数字"),
    IDENT("IDENT", "标识符"),
    BANG("BANG", "!"),
    LPAREN("LPAREN", "("),
    RPAREN("RPAREN", ")"),
    MUL("MUL", "*"),
    DIV("DIV", "/"),
    MOD("MOD", "%"),
    ADD("ADD", "+"),
    SUB("SUB", "-"),
    EQUAL("EQUAL", "=="),
    NOTEQUAL("NOTEQUAL", "!=="),
    GE("GE", ">="),
    LE("LE", "<="),
    GT("GT", ">"),
    LT("LT", "<"),
    AND("AND", "&&"),
    OR("OR", "||");

    private final String code;
    private final String text;
    ExpTokenType(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
