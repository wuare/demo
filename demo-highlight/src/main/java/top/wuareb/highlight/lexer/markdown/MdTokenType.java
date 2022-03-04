package top.wuareb.highlight.lexer.markdown;

public enum MdTokenType {

    WHITE_SPACE("空白符"),
    NEW_LINE("换行符"),

    H1("标题1"),
    H2("标题2"),
    H3("标题3"),
    H4("标题4"),
    H5("标题5"),
    H6("标题6"),
    ALINK("a链接"),
    IMG("图片"),

    TEXT("文本"),

    EOF("结束标志");
    private final String desc;

    MdTokenType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
