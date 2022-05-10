package top.wuareb.highlight.lexer.json;

import top.wuareb.highlight.exception.LexerException;

import java.io.IOException;
import java.io.StringReader;

public class JsonLexer {
    private int ch;
    private int line = 1;
    private int col;
    private JsonToken token;
    private final StringReader reader;

    public JsonLexer(String text) {
        reader = new StringReader(text);
        advance();
    }

    public JsonToken peek() {
        if (token == null) {
            token = nextToken();
        }
        return token;
    }

    public JsonToken nextToken() {
        if (token != null) {
            JsonToken t = token;
            token = null;
            return t;
        }
        if (ch == -1) {
            return new JsonToken(JsonTokenType.EOF, "", line, col);
        }
        if (Character.isWhitespace(ch)) {
            StringBuilder b = new StringBuilder();
            while (Character.isWhitespace(ch)) {
                b.append((char) ch);
                advance();
            }
            return new JsonToken(JsonTokenType.WHITE_SPACE, b.toString(), line, col);
        }
        if (Character.isDigit(ch)) {
            return number();
        }
        switch (ch) {
            case -1:
                return new JsonToken(JsonTokenType.EOF, "", line, col);
            case '[':
                JsonToken t = new JsonToken(JsonTokenType.LBRACKET, "[", line, col);
                advance();
                return t;
            case ']':
                JsonToken t1 = new JsonToken(JsonTokenType.RBRACKET, "]", line, col);
                advance();
                return t1;
            case '{':
                JsonToken t2 = new JsonToken(JsonTokenType.LBRACE, "{", line, col);
                advance();
                return t2;
            case '}':
                JsonToken t3 = new JsonToken(JsonTokenType.RBRACE, "}", line, col);
                advance();
                return t3;
            case ':':
                JsonToken t4 = new JsonToken(JsonTokenType.COLON, ":", line, col);
                advance();
                return t4;
            case ',':
                JsonToken t5 = new JsonToken(JsonTokenType.COMMA, ":", line, col);
                advance();
                return t5;
            case '"':
                return string();
            case 't':
            case 'f':
            case 'n':
                return literal();
            default:
                int tmp = ch;
                advance();
                return new JsonToken(JsonTokenType.TEXT, String.valueOf((char) tmp), line, col);
        }
    }

    private JsonToken number() {
        JsonToken t = new JsonToken(line, col);
        StringBuilder b = new StringBuilder();
        while (Character.isDigit(ch) || ch == '.') {
            b.append((char) ch);
            advance();
        }
        t.setValue(b.toString());
        t.setType(JsonTokenType.NUMBER);
        return t;
    }

    private JsonToken literal() {
        int li = this.line;
        int co = this.col;
        StringBuilder b = new StringBuilder();
        while (ch >= 'a' && ch <= 'z') {
            b.append((char) ch);
            advance();
        }
        String lit = b.toString();
        if ("true".equals(lit)) {
            return new JsonToken(JsonTokenType.TRUE, "true", li, co);
        }
        if ("false".equals(lit)) {
            return new JsonToken(JsonTokenType.FALSE, "false", li, co);
        }
        if ("null".equals(lit)) {
            return new JsonToken(JsonTokenType.NULL, "null", li, co);
        }
        return new JsonToken(JsonTokenType.TEXT, lit, li, co);
    }

    private JsonToken string() {
        int li = this.line;
        int co = this.col;
        StringBuilder builder = new StringBuilder();
        advance();
        // '\u001F'十进制是31，缩写'US'，为单元分隔符
        // 32是空格
        // while ((ch == '\\' || ch > '\u001F') && ch != '"') {
        while (ch != -1 && ch != '"') {

            // escape character
            // 转义字符
            // fragment ESC
            //    : '\\' (["\\/bfnrt] | UNICODE)
            //    ;
            if (ch == '\\') {
                advance();
                if (ch == '"') {
                    builder.append('"');
                    advance();
                    continue;
                }
                if (ch == '\\') {
                    builder.append('\\');
                    advance();
                    continue;
                }
                if (ch == 'b') {
                    builder.append('\b');
                    advance();
                    continue;
                }
                if (ch == 'f') {
                    builder.append('\f');
                    advance();
                    continue;
                }
                if (ch == 'n') {
                    builder.append('\n');
                    advance();
                    continue;
                }
                if (ch == 'r') {
                    builder.append('\r');
                    advance();
                    continue;
                }
                if (ch == 't') {
                    builder.append('\t');
                    advance();
                    continue;
                }
                // fragment UNICODE
                //   : 'u' HEX HEX HEX HEX
                //   ;
                // fragment HEX
                //   : [0-9a-fA-F]
                //   ;
                if (ch == 'u') {
                    StringBuilder hexBuilder = new StringBuilder();
                    advance();
                    for (int i = 0; i < 4; i++) {
                        if ((ch >= '0' && ch <= '9')
                                || (ch >= 'a' && ch <= 'f')
                                || (ch >= 'A' && ch <= 'F')) {
                            hexBuilder.append((char) ch);
                            advance();
                            continue;
                        }
                        // TODO
                        throw new LexerException("unicode转义字符处理错误");
                    }
                    int codePoint = Integer.parseInt(hexBuilder.toString(), 16);
                    char[] chars = Character.toChars(codePoint);
                    builder.append(new String(chars));
                    continue;
                }
            }
            builder.append((char) ch);
            advance();
        }
        if (ch == '"') {
            advance();
        }
        return new JsonToken(JsonTokenType.STRING, builder.toString(), li, co);
    }

    private void advance() {
        try {
            ch = reader.read();
            if (ch == '\n') {
                line++;
                col = 0;
            } else {
                col++;
            }
        } catch (IOException e) {
            throw new LexerException(e);
        }
    }
}
