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
        while (Character.isWhitespace(ch)) {
            nextCh();
        }

        if (ch == '-' || Character.isDigit(ch)) {
            return number();
        }

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

    // json.org grammar
    // number: integer fraction exponent
    // integer: digit
    //        | onenine digits
    //        | '-' digit
    //        | '-' onenine digits
    // digits: digit
    //       | digit digits
    // digit: '0'
    //      | onenine
    // onenine: '1-9'
    // fraction: ""
    //         | '.' digits
    // exponent: ""
    //         | 'E' sign digits
    //         | 'e' sign digits
    // sign: ""
    //     | '+'
    //     | '-'

    // antlr4
    // NUMBER: '-'? INT ('.' [0-9] +)? EXP?
    // INT: '0' | [1-9] [0-9]*
    // EXP: [Ee] [+\-]? INT
    private Token number() {
        StringBuilder builder = new StringBuilder();
        if (ch == '-') {
            builder.append((char) ch);
            nextCh();
        }
        builder.append(nInt());
        if (ch == '.') {
            nextCh();
            do {
                if (!Character.isDigit(ch)) {
                    throw new CommonException("Invalid number, must has digit behind dot");
                }
                builder.append((char) ch);
                nextCh();
            } while (Character.isDigit(ch));
        }
        if (ch == 'E' || ch == 'e') {
            builder.append(nExp());
        }
        return new Token(Token.NUMBER, builder.toString());
    }

    // INT: '0' | [1-9] [0-9]*
    private String nInt() {
        StringBuilder builder = new StringBuilder();
        if (ch == '0') {
            builder.append((char) ch);
            nextCh();
            // skip whitespace and check next character that should be comma or dot or 'E' or 'e'
            while (Character.isWhitespace(ch)) {
                nextCh();
            }
            // may be number only
            if (ch == -1) {
                return builder.toString();
            }
            if (ch != ',' && ch != '.' && ch != 'E' && ch != 'e') {
                throw new CommonException("Invalid number, should not have character behind '0'");
            }
            return builder.toString();
        }
        if (Character.isDigit(ch) && ch != '0') {
            builder.append((char) ch);
            nextCh();
            while (Character.isDigit(ch)) {
                builder.append((char) ch);
                nextCh();
            }
            return builder.toString();
        }
        throw new CommonException("Invalid number, expect '0' or '1-9', but get " + (char) ch);
    }

    // EXP: [Ee] [+\-]? INT
    private String nExp() {
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        nextCh();
        if (ch == '+' || ch == '-') {
            builder.append((char) ch);
            nextCh();
        }
        builder.append(nInt());
        return builder.toString();
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
            // " \ b f n r t
            if (ch == '\\') {
                nextCh();
                if (ch == '\\') {
                    builder.append((char) ch);
                    continue;
                }
                if (ch == 'b') {
                    builder.append('\b');
                    continue;
                }
                if (ch == 'f') {
                    builder.append('\f');
                    continue;
                }
                if (ch == 'n') {
                    builder.append('\n');
                    continue;
                }
                if (ch == 'r') {
                    builder.append('\r');
                    continue;
                }
                if (ch == 't') {
                    builder.append('\t');
                    continue;
                }
                if (ch == '"') {
                    builder.append("\"");
                    continue;
                }
                throw new RuntimeException("Invalid String, unexpect character '\\" + (char) ch + "'");
            }
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
