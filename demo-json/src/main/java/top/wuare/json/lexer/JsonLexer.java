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
        nextCh();
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
        if (ch == -1) {
            return null;
        }
        while (Character.isSpaceChar(ch)) {
            nextCh();
        }

        if (ch == '-' || Character.isDigit(ch)) {
            return number();
        }

        int type;
        switch (ch) {
            case '{':
                Token token = new Token(Token.LBRACE, Character.toString((char) ch));
                nextCh();
                return token;
            case '}':
                Token token1 = new Token(Token.RBRACE, Character.toString((char) ch));
                nextCh();
                return token1;
            case '[':
                Token token2 = new Token(Token.LBRACKET, Character.toString((char) ch));
                nextCh();
                return token2;
            case ']':
                Token token3 = new Token(Token.RBRACKET, Character.toString((char) ch));
                nextCh();
                return token3;
            case ':':
                Token token4 = new Token(Token.COLON, Character.toString((char) ch));
                nextCh();
                return token4;
            case ',':
                Token token5 = new Token(Token.COMMA, Character.toString((char) ch));
                nextCh();
                return token5;
            case '"':
                return string();
            case 't':
                // true
                return literal(Token.LITERAL_TRUE, "true");
            case 'f':
                // false
                return literal(Token.LITERAL_FALSE, "false");
            case 'n':
                // null
                return literal(Token.LITERAL_NULL, "null");
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
        return new Token(Token.NUMBER, builder.toString());
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
                nextCh();
                break;
            }
        }
        return new Token(Token.STRING, builder.toString());
    }

    public int getCh() {
        return ch;
    }
}
