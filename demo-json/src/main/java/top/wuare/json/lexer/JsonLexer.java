package top.wuare.json.lexer;

import top.wuare.json.exception.CommonException;
import top.wuare.json.util.IOUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * JSON Lexer
 *
 * @author wuare
 * @date 2021/6/15
 */
public class JsonLexer {

    private int ch;
    private final Reader reader;

    public JsonLexer(String text) {
        this.reader = new StringReader(text);
    }

    protected void nextCh() {
        try {
            ch = reader.read();
        } catch (IOException e) {
            IOUtil.close(reader);
            throw new CommonException(e);
        }
    }

    public Token nextToken() {
        nextCh();
        if (ch == -1) {
            return null;
        }
        while (Character.isSpaceChar(ch)) {
            nextCh();
        }

        if (ch == '-' || Character.isDigit(ch)) {
            return number();
        }

        switch (ch) {
            case '{':
            case '}':
            case '[':
            case ']':
            case ':':
            case ',':
                return new Token(1, Character.toString((char) ch));
            case '"':
                return string();
            case 't':
                // true
                return literal(4, "true");
            case 'f':
                // false
                return literal(5, "false");
            case 'n':
                // null
                return literal(6, "null");
            default:
                throw new CommonException("the character '" + (char) ch + "' is unexpected, please check it");
        }
    }

    private Token literal(int type, String expect) {
        for (int i = 0; i < expect.length(); i++) {
            if (expect.charAt(i) == ch) {
                nextCh();
                continue;
            }
            throw new CommonException("the character '" + (char) ch + "' is unexpected, please check it");
        }
        return new Token(type, expect);
    }

    private Token number() {
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        for (;;) {
            nextCh();
            if (ch == '.' || Character.isDigit(ch)) {
                builder.append((char) ch);
                continue;
            }
            break;
        }
        return new Token(2, builder.toString());
    }

    private Token string() {
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        for (;;) {
            nextCh();
            if (ch == -1) {
                break;
            }
            // TODO \" Escape character handle?
            builder.append((char) ch);
            if (ch == '"') {
                break;
            }
        }
        return new Token(3, builder.toString());
    }

    public int getCh() {
        return ch;
    }
}
