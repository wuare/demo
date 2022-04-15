package top.wuare.lang.lexer;

public enum TokenType {
    // keywords
    VAR("VAR", "var"),
    FUNC("FUNC", "func"),
    IF("IF", "if"),
    WHILE("WHILE", "while"),
    RETURN("RETURN", "return"),
    ELSE("ELSE", "else"),
    BREAK("BREAK", "break"),
    FOR("FOR", "for"),
    FOREACH("FOREACH", "foreach"),
    IN("IN", "in"),

    NIL("NIL", "nil"),

    NUMBER("NUMBER", "数字"),
    IDENT("IDENT", "标识符"),
    STRING("STRING", "字符串"),
    TRUE("TRUE", "true"),
    FALSE("FALSE", "false"),

    BANG("BANG", "!"),
    LPAREN("LPAREN", "("),
    RPAREN("RPAREN", ")"),
    MUL("MUL", "*"),
    DIV("DIV", "/"),
    MOD("MOD", "%"),
    ADD("ADD", "+"),
    SUB("SUB", "-"),
    ASSIGN("ASSIGN", "="),
    EQUAL("EQUAL", "=="),
    NOTEQUAL("NOTEQUAL", "!="),
    GE("GE", ">="),
    LE("LE", "<="),
    GT("GT", ">"),
    LT("LT", "<"),
    AND("AND", "&&"),
    OR("OR", "||"),

    LBRACE("LBRACE", "{"),
    RBRACE("RBRACE", "}"),
    LBRACKET("LBRACKET", "["),
    RBRACKET("RBRACKET", "]"),
    SEMICOLON("SEMICOLON", ";"),
    COMMA("COMMA", ","),

    COMMENT("COMMENT", "单行注释");

    private final String code;
    private final String text;
    TokenType(String code, String text) {
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
