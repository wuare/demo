package top.wuareb.highlight.lexer.wa;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WaLexer {
    private final Reader reader;
    private int ch;
    private int line = 1;
    private int column;

    private static final Map<String, WaTokenType> RESERVED_KEYWORDS = new HashMap<>();

    static {
        RESERVED_KEYWORDS.put(WaTokenType.VAR.getText(), WaTokenType.VAR);
        RESERVED_KEYWORDS.put(WaTokenType.FUNC.getText(), WaTokenType.FUNC);
        RESERVED_KEYWORDS.put(WaTokenType.IF.getText(), WaTokenType.IF);
        RESERVED_KEYWORDS.put(WaTokenType.WHILE.getText(), WaTokenType.WHILE);
        RESERVED_KEYWORDS.put(WaTokenType.FOR.getText(), WaTokenType.FOR);
        RESERVED_KEYWORDS.put(WaTokenType.BREAK.getText(), WaTokenType.BREAK);
        RESERVED_KEYWORDS.put(WaTokenType.RETURN.getText(), WaTokenType.RETURN);
        RESERVED_KEYWORDS.put(WaTokenType.ELSE.getText(), WaTokenType.ELSE);
        RESERVED_KEYWORDS.put(WaTokenType.TRUE.getText(), WaTokenType.TRUE);
        RESERVED_KEYWORDS.put(WaTokenType.FALSE.getText(), WaTokenType.FALSE);
        RESERVED_KEYWORDS.put(WaTokenType.NIL.getText(), WaTokenType.NIL);
    }

    public WaLexer(String text) {
        reader = new StringReader(text);
        advance();
    }

    public WaLexer(InputStream in) {
        reader = new InputStreamReader(in);
        advance();
    }

    public WaToken nextToken() {
        if (Character.isWhitespace(ch)) {
            WaToken token = new WaToken(WaTokenType.WHITE_SPACE, String.valueOf((char) ch));
            advance();
            return token;
        }
        if (Character.isDigit(ch)) {
            return number();
        }
        if (Character.isLetter(ch) || ch == '_') {
            return ident();
        }
        if (ch == '"') {
            return string();
        }
        switch (ch) {
            case -1:
                return new WaToken(WaTokenType.EOF, "");
            case '(':
                WaToken WaToken0 = new WaToken(WaTokenType.LPAREN, "(");
                advance();
                return WaToken0;
            case ')':
                WaToken WaToken01 = new WaToken(WaTokenType.RPAREN, ")");
                advance();
                return WaToken01;
            case '[':
                WaToken WaToken02 = new WaToken(WaTokenType.LBRACKET, "[");
                advance();
                return WaToken02;
            case ']':
                WaToken WaToken03 = new WaToken(WaTokenType.RBRACKET, "]");
                advance();
                return WaToken03;
            case '{':
                WaToken WaToken04 = new WaToken(WaTokenType.LBRACE, "{");
                advance();
                return WaToken04;
            case '}':
                WaToken WaToken05 = new WaToken(WaTokenType.RBRACE, "}");
                advance();
                return WaToken05;
            case ';':
                WaToken WaToken1 = new WaToken(WaTokenType.SEMICOLON, ";");
                advance();
                return WaToken1;
            case ',':
                WaToken WaToken1a = new WaToken(WaTokenType.COMMA, ",");
                advance();
                return WaToken1a;
            case '!':
                WaToken WaToken2 = new WaToken();
                advance();
                if (ch == '=') {
                    advance();
                    WaToken2.setType(WaTokenType.NOTEQUAL);
                    WaToken2.setText("!=");
                    return WaToken2;
                }
                WaToken2.setType(WaTokenType.BANG);
                WaToken2.setText("!");
                return WaToken2;
            case '+':
                WaToken WaToken3 = new WaToken(WaTokenType.ADD, "+");
                advance();
                return WaToken3;
            case '-':
                WaToken WaToken4 = new WaToken(WaTokenType.SUB, "-");
                advance();
                return WaToken4;
            case '*':
                WaToken WaToken5 = new WaToken(WaTokenType.MUL, "*");
                advance();
                return WaToken5;
            case '/':
                advance();
                if (ch == '/') {
                    advance();
                    StringBuilder builder = new StringBuilder("//");
                    while (ch != -1 && ch != '\n') {
                        builder.append((char) ch);
                        advance();
                    }
                    return new WaToken(WaTokenType.COMMENT, builder.toString());
                }
                return new WaToken(WaTokenType.DIV, "/");
            case '%':
                WaToken WaToken7 = new WaToken(WaTokenType.MOD, "%");
                advance();
                return WaToken7;
            case '>':
                WaToken WaToken8 = new WaToken();
                advance();
                if (ch == '=') {
                    advance();
                    WaToken8.setType(WaTokenType.GE);
                    WaToken8.setText(">=");
                    return WaToken8;
                }
                WaToken8.setType(WaTokenType.GT);
                WaToken8.setText(">");
                return WaToken8;
            case '<':
                WaToken WaToken9 = new WaToken();
                advance();
                if (ch == '=') {
                    advance();
                    WaToken9.setType(WaTokenType.LE);
                    WaToken9.setText("<=");
                    return WaToken9;
                }
                WaToken9.setType(WaTokenType.LT);
                WaToken9.setText("<");
                return WaToken9;
            case '=':
                WaToken WaToken10 = new WaToken();
                advance();
                if (ch == '=') {
                    advance();
                    WaToken10.setType(WaTokenType.EQUAL);
                    WaToken10.setText("==");
                    return WaToken10;
                }
                WaToken10.setType(WaTokenType.ASSIGN);
                WaToken10.setText("=");
                return WaToken10;
            case '&':
                WaToken WaToken11 = new WaToken();
                advance();
                if (ch != '&') {
                    throw new RuntimeException("syntax error, '&' is invalid, at line: " + line + ", column: " + column);
                }
                advance();
                WaToken11.setType(WaTokenType.AND);
                WaToken11.setText("&&");
                return WaToken11;
            case '|':
                WaToken WaToken12 = new WaToken();
                advance();
                if (ch != '|') {
                    throw new RuntimeException("syntax error, '|' is invalid, at line: " + line + ", column: " + column);
                }
                advance();
                WaToken12.setType(WaTokenType.OR);
                WaToken12.setText("||");
                return WaToken12;
            default:
                int tmp = ch;
                advance();
                return new WaToken(WaTokenType.TEXT, String.valueOf((char) tmp));
        }
    }

    private WaToken string() {
        WaToken WaToken = new WaToken();
        StringBuilder builder = new StringBuilder();
        advance(); // consume first double quotation mark
        while (ch != -1 && ch != '"') {
            builder.append((char) ch);
            advance();
        }
        advance();
        WaToken.setType(WaTokenType.STRING);
        WaToken.setText(builder.toString());
        return WaToken;
    }

    private WaToken ident() {
        WaToken WaToken = new WaToken();
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        advance(); // consume first letter
        while (Character.isLetter(ch) || ch == '_' || ch == '$' || Character.isDigit(ch)) {
            builder.append((char) ch);
            advance();
        }
        WaToken.setType(WaTokenType.IDENT);
        String text = builder.toString();
        if (RESERVED_KEYWORDS.get(text) != null) {
            WaToken.setType(RESERVED_KEYWORDS.get(text));
        }
        WaToken.setText(text);
        return WaToken;
    }

    private WaToken number() {
        WaToken WaToken = new WaToken();
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        advance(); // consume first letter
        while (Character.isDigit(ch) || ch == '.') {
            builder.append((char) ch);
            advance();
        }
        WaToken.setType(WaTokenType.NUMBER);
        WaToken.setText(builder.toString());
        return WaToken;
    }

    public void advance() {
        try {
            ch = reader.read();
            if (ch == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
