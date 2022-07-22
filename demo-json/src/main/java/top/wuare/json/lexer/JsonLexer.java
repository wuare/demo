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
 * @since 2021/6/15
 */
public class JsonLexer {

    private int ch;
    private final Reader reader;
    private int line = 1;
    private int column;

    public JsonLexer(String text) {
        this.reader = new StringReader(text);
        nextCh();
    }

    protected void nextCh() {
        try {
            ch = reader.read();
            if (ch == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
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
            case -1:
                return null;
            case '{':
                Token token = new Token(TokenType.LBRACE, Character.toString((char) ch), line, column);
                nextCh();
                return token;
            case '}':
                Token token1 = new Token(TokenType.RBRACE, Character.toString((char) ch), line, column);
                nextCh();
                return token1;
            case '[':
                Token token2 = new Token(TokenType.LBRACKET, Character.toString((char) ch), line, column);
                nextCh();
                return token2;
            case ']':
                Token token3 = new Token(TokenType.RBRACKET, Character.toString((char) ch), line, column);
                nextCh();
                return token3;
            case ':':
                Token token4 = new Token(TokenType.COLON, Character.toString((char) ch), line, column);
                nextCh();
                return token4;
            case ',':
                Token token5 = new Token(TokenType.COMMA, Character.toString((char) ch), line, column);
                nextCh();
                return token5;
            case '"':
                return string();
            case 't':
                // true
                return literal(TokenType.LITERAL_TRUE, "true");
            case 'f':
                // false
                return literal(TokenType.LITERAL_FALSE, "false");
            case 'n':
                // null
                return literal(TokenType.LITERAL_NULL, "null");
            default:
                throw new CommonException("the character '" + (char) ch + "' at line: " + line + ", column: " + column + " is unexpected, please check it");
        }
    }

    private Token literal(TokenType type, String expect) {
        int li = this.line;
        int co = this.column;
        for (int i = 0; i < expect.length(); i++) {
            if (expect.charAt(i) == ch) {
                nextCh();
                continue;
            }
            throw new CommonException("the character '" + (char) ch + "' at line: " + line + ", column: " + column + " is unexpected, please check it");
        }
        return new Token(type, expect, li, co);
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
        int li = this.line;
        int co = this.column;
        StringBuilder builder = new StringBuilder();
        if (ch == '-') {
            builder.append((char) ch);
            nextCh();
        }
        builder.append(nInt());
        if (ch == '.') {
            builder.append((char) ch);
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
        return new Token(TokenType.NUMBER, builder.toString(), li, co);
    }

    // INT: '0' | [1-9] [0-9]*
    private String nInt() {
        StringBuilder builder = new StringBuilder();
        if (ch == '0') {
            builder.append((char) ch);
            nextCh();
            // maybe we should not check, because check whether it is legal in phase of syntax analysis
            // skip whitespace and check next character that should be comma or dot or 'E' or 'e'
            while (Character.isWhitespace(ch)) {
                nextCh();
            }
            // may be number only
            if (ch == -1) {
                return builder.toString();
            }
            if (ch != ',' && ch != '.' && ch != 'E' && ch != 'e' && ch != '}') {
                throw new CommonException("Invalid number at line: " + line + ", column: " + column + ", should not have character behind '0'");
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
        throw new CommonException("Invalid number at line: " + line + ", column: " + column + ", expect '0' or '1-9', but get " + (char) ch);
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

    // antlr4
    // STRING
    //   : '"' (ESC | SAFECODEPOINT)* '"'
    //   ;
    // fragment ESC
    //   : '\\' (["\\/bfnrt] | UNICODE)
    //   ;
    // fragment UNICODE
    //   : 'u' HEX HEX HEX HEX
    //   ;
    // fragment HEX
    //   : [0-9a-fA-F]
    //   ;
    // fragment SAFECODEPOINT
    //   : ~ ["\\\u0000-\u001F]
    //   ;
    private Token string() {
        int li = this.line;
        int co = this.column;
        StringBuilder builder = new StringBuilder();
        nextCh(); // consume first double quotation mark
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
                nextCh();
                if (ch == '"') {
                    builder.append('"');
                    nextCh();
                    continue;
                }
                if (ch == '\\') {
                    builder.append('\\');
                    nextCh();
                    continue;
                }
                // TODO '/'?

                if (ch == 'b') {
                    builder.append('\b');
                    nextCh();
                    continue;
                }
                if (ch == 'f') {
                    builder.append('\f');
                    nextCh();
                    continue;
                }
                if (ch == 'n') {
                    builder.append('\n');
                    nextCh();
                    continue;
                }
                if (ch == 'r') {
                    builder.append('\r');
                    nextCh();
                    continue;
                }
                if (ch == 't') {
                    builder.append('\t');
                    nextCh();
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
                    nextCh();
                    for (int i = 0; i < 4; i++) {
                        if ((ch >= '0' && ch <= '9')
                                || (ch >= 'a' && ch <= 'f')
                                || (ch >= 'A' && ch <= 'F')) {
                            hexBuilder.append((char) ch);
                            nextCh();
                            continue;
                        }
                        throw new CommonException("Invalid unicode character at line: " + line + ", column: " + column + ", unexpect character '" + (char) ch + "'");
                    }
                    int codePoint = Integer.parseInt(hexBuilder.toString(), 16);
                    char[] chars = Character.toChars(codePoint);
                    builder.append(new String(chars));
                    continue;
                }
            }
            builder.append((char) ch);
            nextCh();
            // TODO
            // check character greater than '\u001F' in antlr4's grammar,
            // but this is invalid when string has space etc character.
            // the string can be has space character when i use gson reader parse string,
            // so not check there, perhaps will fix it one day, hope so.

            // if (ch > '\u001F') {
            //     builder.append((char) ch);
            //     nextCh();
            //     continue;
            // }
            // throw new CommonException("Invalid String, unexpect character '" + (char) ch + "'");
        }
        if (ch == '"') {
            nextCh();
            return new Token(TokenType.STRING, builder.toString(), li, co);
        }
        throw new CommonException("Invalid String at line: " + line + ", column: " + column + ", unexpect character '" + (char) ch + "'");
    }

    public int getCh() {
        return ch;
    }
}
