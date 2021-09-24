package top.wuareb.highlight.lexer.markdown;

public enum MdTokenType {
    ID("ID");
    private final String literal;

    MdTokenType(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
