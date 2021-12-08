package top.wuare.lang.lexer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

    private final Reader reader;
    private int ch;
    private int line = 1;
    private int column;

    private static final Map<String, TokenType> RESERVED_KEYWORDS = new HashMap<>();
    static {
        RESERVED_KEYWORDS.put(TokenType.VAR.getText(), TokenType.VAR);
        RESERVED_KEYWORDS.put(TokenType.FUNC.getText(), TokenType.FUNC);
        RESERVED_KEYWORDS.put(TokenType.IF.getText(), TokenType.IF);
        RESERVED_KEYWORDS.put(TokenType.WHILE.getText(), TokenType.WHILE);
        RESERVED_KEYWORDS.put(TokenType.RETURN.getText(), TokenType.RETURN);
        RESERVED_KEYWORDS.put(TokenType.ELSE.getText(), TokenType.ELSE);
    }

    public Lexer(String text) {
        reader = new StringReader(text);
        advance();
    }

    public Lexer(InputStream in) {
        reader = new InputStreamReader(in);
        advance();
    }

    public Token nextToken() {
        while (Character.isWhitespace(ch)) {
            advance();
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
                return null;
            case '(':
                Token Token0 = new Token(TokenType.LPAREN, "(", line, column);
                advance();
                return Token0;
            case ')':
                Token Token01 = new Token(TokenType.RPAREN, ")", line, column);
                advance();
                return Token01;
            case '[':
                Token Token02 = new Token(TokenType.LBRACKET, "[", line, column);
                advance();
                return Token02;
            case ']':
                Token Token03 = new Token(TokenType.RBRACKET, "]", line, column);
                advance();
                return Token03;
            case '{':
                Token Token04 = new Token(TokenType.LBRACE, "{", line, column);
                advance();
                return Token04;
            case '}':
                Token Token05 = new Token(TokenType.RBRACE, "}", line, column);
                advance();
                return Token05;
            case ';':
                Token Token1 = new Token(TokenType.SEMICOLON, ";", line, column);
                advance();
                return Token1;
            case ',':
                Token Token1a = new Token(TokenType.COMMA, ",", line, column);
                advance();
                return Token1a;
            case '!':
                Token Token2 = new Token(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    Token2.setType(TokenType.NOTEQUAL);
                    Token2.setText("!=");
                    return Token2;
                }
                Token2.setType(TokenType.BANG);
                Token2.setText("!");
                return Token2;
            case '+':
                Token Token3 = new Token(TokenType.ADD, "+", line, column);
                advance();
                return Token3;
            case '-':
                Token Token4 = new Token(TokenType.SUB, "-", line, column);
                advance();
                return Token4;
            case '*':
                Token Token5 = new Token(TokenType.MUL, "*", line, column);
                advance();
                return Token5;
            case '/':
                Token Token6 = new Token(TokenType.DIV, "/", line, column);
                advance();
                return Token6;
            case '%':
                Token Token7 = new Token(TokenType.MOD, "%", line, column);
                advance();
                return Token7;
            case '>':
                Token Token8 = new Token(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    Token8.setType(TokenType.GE);
                    Token8.setText(">=");
                    return Token8;
                }
                Token8.setType(TokenType.GT);
                Token8.setText(">");
                return Token8;
            case '<':
                Token Token9 = new Token(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    Token9.setType(TokenType.LE);
                    Token9.setText("<=");
                    return Token9;
                }
                Token9.setType(TokenType.LT);
                Token9.setText("<");
                return Token9;
            case '=':
                Token Token10 = new Token(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    Token10.setType(TokenType.EQUAL);
                    Token10.setText("==");
                    return Token10;
                }
                advance();
                Token10.setType(TokenType.ASSIGN);
                Token10.setText("=");
                return Token10;
            case '&':
                Token Token11 = new Token(line, column);
                advance();
                if (ch != '&') {
                    throw new RuntimeException("syntax error, '&' is invalid, at line: " + line + ", column: " + column);
                }
                advance();
                Token11.setType(TokenType.AND);
                Token11.setText("&&");
                return Token11;
            case '|':
                Token Token12 = new Token(line, column);
                advance();
                if (ch != '|') {
                    throw new RuntimeException("syntax error, '|' is invalid, at line: " + line + ", column: " + column);
                }
                advance();
                Token12.setType(TokenType.OR);
                Token12.setText("||");
                return Token12;
        }
        throw new RuntimeException("syntax error, at line: " + line + ", column: " + column);
    }

    private Token string() {
        Token token = new Token(line, column);
        StringBuilder builder = new StringBuilder();
        advance(); // consume first double quotation mark
        while (ch != -1 && ch != '"') {
            builder.append((char) ch);
            advance();
        }
        if (ch != '"') {
            throw new RuntimeException("syntax error, '\"' is excepted, at line: " + line + ", column: " + column);
        }
        advance();
        token.setType(TokenType.STRING);
        token.setText(builder.toString());
        return token;
    }

    private Token ident() {
        Token token = new Token(line, column);
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        advance(); // consume first letter
        while (Character.isLetter(ch) || ch == '_' || ch == '$' || Character.isDigit(ch)) {
            builder.append((char) ch);
            advance();
        }
        token.setType(TokenType.IDENT);
        String text = builder.toString();
        if (RESERVED_KEYWORDS.get(text) != null) {
            token.setType(RESERVED_KEYWORDS.get(text));
        }
        token.setText(text);
        return token;
    }

    private Token number() {
        Token token = new Token(line, column);
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        advance(); // consume first letter
        while (Character.isDigit(ch) || ch == '.') {
            builder.append((char) ch);
            advance();
        }
        token.setType(TokenType.NUMBER);
        token.setText(builder.toString());
        return token;
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
