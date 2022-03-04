package top.wuareb.highlight.lexer.markdown;

import top.wuareb.highlight.exception.LexerException;

import java.io.IOException;
import java.io.StringReader;

public class MdLexer {
    private int ch;
    private int line = 1;
    private int column;
    private final StringReader reader;

    public MdLexer(String text) {
        this.reader = new StringReader(text);
        advance();
    }

    public MdToken nextToken() {
        if (ch == '\n') {
            advance();
            return new MdToken(MdTokenType.NEW_LINE, "\n");
        }
        if (Character.isWhitespace(ch)) {
            StringBuilder builder = new StringBuilder().append((char) ch);
            advance();
            while (Character.isWhitespace(ch)) {
                builder.append((char) ch);
                advance();
            }
            return new MdToken(MdTokenType.WHITE_SPACE, builder.toString());
        }
        if (ch == '#') {
            StringBuilder builder = new StringBuilder("#");
            advance();
            while (ch == '#') {
                builder.append("#");
                advance();
            }
            int len = builder.length();
            if (ch == ' ' && len <= 6) {
                advance();
                // 说明是标题 提取标题内容
                StringBuilder hcBuilder = new StringBuilder();
                while (ch != -1 && ch != '\n') {
                    hcBuilder.append((char) ch);
                    advance();
                }
                return new MdToken(headType(len), hcBuilder.toString());
            }
            while (ch != -1 && ch != '\n') {
                builder.append((char) ch);
                advance();
            }
            return new MdToken(MdTokenType.TEXT, builder.toString());
        }
        switch (ch) {
            case -1:
                return new MdToken(MdTokenType.EOF, "");
            case '[':
                return aLinkOrText();
            case '!':
                return imgOrText();
            default:
                StringBuilder builder = new StringBuilder();
                while (ch != -1 && ch != '\n' && ch != '!' && ch != '[') {
                    builder.append((char) ch);
                    advance();
                }
                return new MdToken(MdTokenType.TEXT, builder.toString());
        }
    }

    private MdToken aLinkOrText() {
        StringBuilder txtB = new StringBuilder("[");
        advance();
        StringBuilder aTxtB = new StringBuilder("[");
        while (ch != -1 && ch != '\n') {
            aTxtB.append((char) ch);
            txtB.append((char) ch);
            if (ch == ']') {
                advance();
                if (ch == '(') {
                    txtB.append("(");
                    advance();
                    StringBuilder aHrefB = new StringBuilder("(");
                    while (ch != -1 && ch != '\n') {
                        aHrefB.append((char) ch);
                        txtB.append((char) ch);
                        if (ch == ')') {
                            advance();
                            MdToken alink = new MdToken(MdTokenType.ALINK, aTxtB.toString());
                            alink.setLink(aHrefB.toString());
                            return alink;
                        }
                        advance();
                    }
                }
            }
            advance();
        }
        return new MdToken(MdTokenType.TEXT, txtB.toString());
    }

    private MdToken imgOrText() {
        advance();
        if (ch == '[') {
            MdToken t = aLinkOrText();
            if (t.getType() == MdTokenType.ALINK) {
                return new MdToken(MdTokenType.IMG, t.getText(), t.getLink());
            }
            return new MdToken(MdTokenType.TEXT, "!" + t.getText());
        }
        return new MdToken(MdTokenType.TEXT, "!");
    }

    private void advance() {
        try {
            ch = reader.read();
            if (ch == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        } catch (IOException e) {
            throw new LexerException(e);
        }
    }

    private MdTokenType headType(int len) {
        switch (len) {
            case 2:
                return MdTokenType.H2;
            case 3:
                return MdTokenType.H3;
            case 4:
                return MdTokenType.H4;
            case 5:
                return MdTokenType.H5;
            case 6:
                return MdTokenType.H6;
            default:
                return MdTokenType.H1;
        }
    }
}
