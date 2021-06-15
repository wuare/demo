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
            // number TODO
            return null;
        }

        switch (ch) {
            case '{':
            case '}':
            case '[':
            case ']':
            case ':':
                return new Token(1, Character.toString((char) ch));
            case '"':
                // string TODO
            case 't':
                // true TODO
            case 'f':
                // false TODO
            case 'n':
                // null TODO
            default:
                throw new CommonException("the character '" + (char) ch + "' is unexpected, please check it");
        }
    }

    public int getCh() {
        return ch;
    }
}
