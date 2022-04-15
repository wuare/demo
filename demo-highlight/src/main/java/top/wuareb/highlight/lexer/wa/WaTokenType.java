package top.wuareb.highlight.lexer.wa;

public enum WaTokenType {
    // keywords
    VAR("VAR", "var"),
    FUNC("FUNC", "func"),
    IF("IF", "if"),
    WHILE("WHILE", "while"),
    FOR("FOR", "for"),
    BREAK("BREAK", "break"),
    RETURN("RETURN", "return"),
    ELSE("ELSE", "else"),
    FOREACH("FOREACH", "foreach"),
    IN("IN", "in"),

    TRUE("TRUE", "true"),
    FALSE("FALSE", "false"),
    NIL("NIL", "nil"),

    NUMBER("NUMBER", "数字"),
    IDENT("IDENT", "标识符"),
    STRING("STRING", "字符串"),
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
    NOTEQUAL("NOTEQUAL", "!=="),
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

    WHITE_SPACE("WHITE_SPACE", "空白"),
    COMMENT("COMMENT", "单行注释"),

    TEXT("TEXT", "文本"),
    EOF("EOF", "EOF");

    private final String code;
    private final String text;
    WaTokenType(String code, String text) {
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
