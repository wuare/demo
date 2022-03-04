package top.wuareb.highlight.lexer.markdown;

public class MdToken {

    private MdTokenType type;
    private String text;
    private String link;

    public MdToken() {
    }

    public MdToken(MdTokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public MdToken(MdTokenType type, String text, String link) {
        this.type = type;
        this.text = text;
        this.link = link;
    }

    public MdTokenType getType() {
        return type;
    }

    public void setType(MdTokenType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "MdToken{" +
                "type=" + type +
                ", text='" + text + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
