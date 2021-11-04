package top.wuare.express;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ExpLexer {

    private final Reader reader;
    private int ch;
    private int line = 1;
    private int column;

    public ExpLexer(String text) {
        this.reader = new StringReader(text);
        advance();
    }

    public ExpToken nextToken() {
        while (Character.isWhitespace(ch)) {
            advance();
        }
        if (Character.isDigit(ch)) {
            return number();
        }
        if (Character.isLetter(ch) || ch == '_') {
            return ident();
        }
        switch (ch) {
            case -1:
                return null;
            case '(':
                ExpToken expToken0 = new ExpToken(ExpTokenType.LPAREN, "(", line, column);
                advance();
                return expToken0;
            case ')':
                ExpToken expToken1 = new ExpToken(ExpTokenType.RPAREN, ")", line, column);
                advance();
                return expToken1;
            case '!':
                ExpToken expToken2 = new ExpToken(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    expToken2.setType(ExpTokenType.NOTEQUAL);
                    expToken2.setText("!=");
                    return expToken2;
                }
                expToken2.setType(ExpTokenType.BANG);
                expToken2.setText("!");
                return expToken2;
            case '+':
                ExpToken expToken3 = new ExpToken(ExpTokenType.ADD, "+", line, column);
                advance();
                return expToken3;
            case '-':
                ExpToken expToken4 = new ExpToken(ExpTokenType.SUB, "+", line, column);
                advance();
                return expToken4;
            case '*':
                ExpToken expToken5 = new ExpToken(ExpTokenType.MUL, "*", line, column);
                advance();
                return expToken5;
            case '/':
                ExpToken expToken6 = new ExpToken(ExpTokenType.DIV, "/", line, column);
                advance();
                return expToken6;
            case '%':
                ExpToken expToken7 = new ExpToken(ExpTokenType.MOD, "%", line, column);
                advance();
                return expToken7;
            case '>':
                ExpToken expToken8 = new ExpToken(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    expToken8.setType(ExpTokenType.GE);
                    expToken8.setText(">=");
                    return expToken8;
                }
                expToken8.setType(ExpTokenType.GT);
                expToken8.setText(">");
                return expToken8;
            case '<':
                ExpToken expToken9 = new ExpToken(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    expToken9.setType(ExpTokenType.LE);
                    expToken9.setText("<=");
                    return expToken9;
                }
                expToken9.setType(ExpTokenType.LT);
                expToken9.setText("<");
                return expToken9;
            case '=':
                ExpToken expToken10 = new ExpToken(line, column);
                advance();
                if (ch != '=') {
                    throw new RuntimeException("syntax error, '=' is invalid, at line: " + line + ", column: " + column);
                }
                advance();
                expToken10.setType(ExpTokenType.EQUAL);
                expToken10.setText("==");
                return expToken10;
            case '&':
                ExpToken expToken11 = new ExpToken(line, column);
                advance();
                if (ch != '&') {
                    throw new RuntimeException("syntax error, '&' is invalid, at line: " + line + ", column: " + column);
                }
                advance();
                expToken11.setType(ExpTokenType.ADD);
                expToken11.setText("&&");
                return expToken11;
            case '|':
                ExpToken expToken12 = new ExpToken(line, column);
                advance();
                if (ch != '|') {
                    throw new RuntimeException("syntax error, '|' is invalid, at line: " + line + ", column: " + column);
                }
                advance();
                expToken12.setType(ExpTokenType.OR);
                expToken12.setText("||");
                return expToken12;
        }
        throw new RuntimeException("syntax error, at line: " + line + ", column: " + column);
    }

    private ExpToken ident() {
        ExpToken token = new ExpToken(line, column);
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        advance(); // consume first letter
        while (Character.isLetter(ch) || ch == '_' || ch == '$' || Character.isDigit(ch)) {
            builder.append((char) ch);
            advance();
        }
        token.setType(ExpTokenType.IDENT);
        token.setText(builder.toString());
        return token;
    }

    private ExpToken number() {
        ExpToken token = new ExpToken(line, column);
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        advance(); // consume first letter
        while (Character.isDigit(ch) || ch == '.') {
            builder.append((char) ch);
            advance();
        }
        token.setType(ExpTokenType.NUMBER);
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
