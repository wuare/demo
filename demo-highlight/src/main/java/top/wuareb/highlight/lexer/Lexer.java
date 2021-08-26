package top.wuareb.highlight.lexer;

import top.wuareb.highlight.exception.LexerException;
import top.wuareb.highlight.util.Sets;

import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

public class Lexer {
    private int ch;
    private int line = 1;
    private int column;
    private final StringReader reader;

    private final Set<String> keywords = Sets.of("abstract", "assert", "boolean",
            "break", "byte", "case", "catch", "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "final", "finally", "float", "for", "if", "goto", "implements", "import",
            "instanceof", "int", "interface", "long", "native", "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while", "null", "true", "false");

    public Lexer(String text) {
        reader = new StringReader(text);
        advance();
    }

    public Token nextToken() {
        if (ch == -1) {
            return null;
        }
        // skip control character
        while (ch >= 0 && ch <= 31 && ch != '\r' && ch != '\n') {
            advance();
        }
        // skip while space
        if (Character.isWhitespace(ch)) {
            StringBuilder spaceBuilder = new StringBuilder();
            Token spaceToken = new Token(line, column);
            while (Character.isWhitespace(ch)) {
                spaceBuilder.append((char) ch);
                advance();
            }
            spaceToken.setType(Token.WHITE_SPACE);
            spaceToken.setValue(spaceBuilder.toString());
            // 特定场景，高亮需保存空白符，如果是编译构造语法数，空白符不保留
            return spaceToken;
        }

        // IDENTIFIER
        if (Character.isLetter(ch) || ch == '$' || ch == '_') {
            return identifier();
        }
        // NUMBER
        if (Character.isDigit(ch)) {
            return number();
        }
        while (ch >= 0 && ch <= 31 && ch != '\r' && ch != '\n') {
            advance();
        }
        switch (ch) {
            case -1:
                return null;
            case '\'':
                return charLiteral();
            case '"':
                return stringLiteral();
            case '/':
                return comment();
            case '(':
                Token token3 = new Token(Token.LPAREN, "(", line, column);
                advance();
                return token3;
            case ')':
                Token token4 = new Token(Token.RPAREN, ")", line, column);
                advance();
                return token4;
            case '{':
                Token token5 = new Token(Token.LBRACE, "{", line, column);
                advance();
                return token5;
            case '}':
                Token token6 = new Token(Token.RBRACE, "}", line, column);
                advance();
                return token6;
            case '[':
                Token token7 = new Token(Token.LBRACKET, "[", line, column);
                advance();
                return token7;
            case ']':
                Token token8 = new Token(Token.RBRACKET, "]", line, column);
                advance();
                return token8;
            case ';':
                Token token9 = new Token(Token.SEMI, ";", line, column);
                advance();
                return token9;
            case ',':
                Token token10 = new Token(Token.COMMA, ",", line, column);
                advance();
                return token10;
            case '.':
                Token token11 = new Token(Token.DOT, ".", line, column);
                advance();
                return token11;
            case '=':
                // '=', '=='
                return assign_2('=', Token.ASSIGN, Token.EQUAL);
            case '>':
                Token token12 = new Token(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    token12.setType(Token.GE);
                    token12.setValue(">=");
                    return token12;
                }
                if (ch == '>') {
                    advance();
                    if (ch == '>') {
                        advance();
                        if (ch == '=') {
                            advance();
                            token12.setType(Token.U_R_SHIFT_ASSIGN);
                            token12.setValue(">>>=");
                            return token12;
                        }
                        token12.setType(Token.U_R_SHIFT);
                        token12.setValue(">>>");
                        return token12;
                    }
                    if (ch == '=') {
                        advance();
                        token12.setType(Token.R_SHIFT_ASSIGN);
                        token12.setValue(">>=");
                        return token12;
                    }
                    token12.setType(Token.R_SHIFT);
                    token12.setValue(">>");
                    return token12;
                }
                token12.setType(Token.GT);
                token12.setValue(">");
                return token12;
            case '<':
                Token token13 = new Token(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    token13.setType(Token.LE);
                    token13.setValue("<=");
                    return token13;
                }
                if (ch == '<') {
                    advance();
                    if (ch == '=') {
                        advance();
                        token13.setType(Token.L_SHIFT_ASSIGN);
                        token13.setValue("<<=");
                        return token13;
                    }
                    token13.setType(Token.L_SHIFT);
                    token13.setValue("<<");
                    return token13;
                }
                token13.setType(Token.LT);
                token13.setValue("<");
                return token13;
            case '!':
                Token token14 = new Token(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    token14.setType(Token.NOTEQUAL);
                    token14.setValue("!=");
                    return token14;
                }
                token14.setType(Token.BANG);
                token14.setValue("!");
                return token14;
            case '~':
                Token token15 = new Token(Token.TILDE, "~", line, column);
                advance();
                return token15;
            case '?':
                Token token16 = new Token(Token.QUESTION, "?", line, column);
                advance();
                return token16;
            case ':':
                Token token17 = new Token(Token.COLON, ":", line, column);
                advance();
                return token17;
            case '&':
                Token token18 = new Token(line, column);
                advance();
                if (ch == '&') {
                    advance();
                    token18.setType(Token.AND);
                    token18.setValue("&&");
                    return token18;
                }
                if (ch == '=') {
                    advance();
                    token18.setType(Token.AND_ASSIGN);
                    token18.setValue("&=");
                    return token18;
                }
                token18.setType(Token.BIT_AND);
                token18.setValue("&");
                return token18;
            case '|':
                Token token19 = new Token(line, column);
                advance();
                if (ch == '|') {
                    advance();
                    token19.setType(Token.OR);
                    token19.setValue("||");
                    return token19;
                }
                if (ch == '=') {
                    advance();
                    token19.setType(Token.OR_ASSIGN);
                    token19.setValue("|=");
                    return token19;
                }
                token19.setType(Token.BIT_OR);
                token19.setValue("|");
                return token19;
            case '+':
                Token token20 = new Token(line, column);
                advance();
                if (ch == '+') {
                    advance();
                    token20.setType(Token.INC);
                    token20.setValue("++");
                    return token20;
                }
                if (ch == '=') {
                    advance();
                    token20.setType(Token.ADD_ASSIGN);
                    token20.setValue("+=");
                    return token20;
                }
                token20.setType(Token.ADD);
                token20.setValue("+");
                return token20;
            case '-':
                Token token21 = new Token(line, column);
                advance();
                if (ch == '-') {
                    advance();
                    token21.setType(Token.DEC);
                    token21.setValue("--");
                    return token21;
                }
                if (ch == '=') {
                    advance();
                    token21.setType(Token.SUB_ASSIGN);
                    token21.setValue("-=");
                    return token21;
                }
                token21.setType(Token.SUB);
                token21.setValue("-");
                return token21;
            case '*':
                return assign_2('=', Token.MUL, Token.MUL_ASSIGN);
            case '^':
                return assign_2('=', Token.CARET, Token.XOR_ASSIGN);
            case '%':
                return assign_2('=', Token.MOD, Token.MOD_ASSIGN);
            case '@':
                Token token22 = new Token(Token.AT, "@", line, column);
                advance();
                return token22;
            default:
                throw new LexerException("syntax error at line: " + line + ", column " + column + ", unexpect character: '" + (char) ch + "'");
        }
    }

    // COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
    // LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);
    private Token comment() {
        StringBuilder builder = new StringBuilder();
        Token token = new Token(line, column);
        advance();
        if (ch == '*') {
            builder.append("/*");
            advance();
            builder.append((char) ch);
            int lastCh = ch;
            while (true) {
                if (ch == -1) {
                    throw new LexerException("syntax error at line: "
                            + line + ", column " + column + ", unexpect character EOF");
                }
                advance();
                builder.append((char) ch);
                if (lastCh == '*' && ch == '/') {
                    break;
                }
                lastCh = ch;
            }
            advance();
            token.setType(Token.COMMENT);
            token.setValue(builder.toString());
            return token;
        }
        if (ch == '/') {
            advance();
            builder.append("//");
            while (ch != '\r' && ch != '\n' && ch != -1) {
                builder.append((char) ch);
                advance();
            }
            token.setType(Token.LINE_COMMENT);
            token.setValue(builder.toString());
            return token;
        }
        if (ch == '=') {
            token.setType(Token.DIV_ASSIGN);
            token.setValue("/=");
            return token;
        }
        token.setType(Token.DIV);
        token.setValue("/");
        return token;
    }

    // fragment EscapeSequence
    //    : '\\' [btnfr"'\\]
    //    | '\\' ([0-3]? [0-7])? [0-7]
    //    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
    //    ;

    // CHAR_LITERAL:       '\'' (~['\\\r\n] | EscapeSequence) '\'';
    private Token charLiteral() {
        StringBuilder builder = new StringBuilder();
        Token token = new Token(line, column);
        advance();
        if (ch == '\r' || ch == '\n') {
            throw new LexerException("syntax error at line: " + line + ", column " + column + ", unexpect character: '\\r' or '\\n'");
        }
        if (ch == '\\') {
            escape(builder);
        } else {
            builder.append((char) ch);
            advance();
        }
        if (ch != '\'') {
            throw new LexerException("syntax error at line: " + line + ", column " + column
                    + ", expect character: ''', but get: '" + (char) ch + "'");
        }
        token.setType(Token.CHAR_LITERAL);
        token.setValue(builder.toString());
        advance();
        return token;
    }

    // STRING_LITERAL:     '"' (~["\\\r\n] | EscapeSequence)* '"';
    private Token stringLiteral() {
        StringBuilder builder = new StringBuilder();
        Token token = new Token(line, column);
        advance();
        while (ch != '"' && ch != '\r' && ch != '\n' && ch != -1) {
            if (ch == '\\') {
                escape(builder);
                continue;
            }
            builder.append((char) ch);
            advance();
        }
        if (ch != '"') {
            throw new LexerException("syntax error at line: " + line + ", column " + column
                    + ", expect character: '\"', but get: '" + (char) ch + "'");
        }
        token.setType(Token.STRING_LITERAL);
        token.setValue(builder.toString());
        advance();
        return token;
    }

    private void escape(StringBuilder builder) {
        if (ch == '\\') {
            advance();
            if (ch == 'b') {
                advance();
                builder.append('\b');
                return;
            }
            if (ch == 't') {
                advance();
                builder.append('\t');
                return;
            }
            if (ch == 'n') {
                advance();
                builder.append('\n');
                return;
            }
            if (ch == 'f') {
                advance();
                builder.append('\f');
                return;
            }
            if (ch == 'r') {
                advance();
                builder.append('\r');
                return;
            }
            if (ch == '\'') {
                advance();
                builder.append('\'');
                return;
            }
            if (ch == '\\') {
                advance();
                builder.append('\\');
                return;
            }
            if (ch == '"') {
                advance();
                builder.append('"');
                return;
            }
            if (ch == 'u') {
                advance();
                for (int i = 0; i < 4; i++) {
                    if (Character.isDigit(ch) || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
                        builder.append((char) ch);
                        advance();
                        continue;
                    }
                    throw new LexerException("syntax error at line: " + line + ", column " + column
                            + ", unexpect character: '" + (char) ch + "'");
                }
            }
        }
    }

    private Token assign_2(int nextCh, int curType, int nextType) {
        Token token = new Token(line, column);
        String val = (char) ch + "";
        token.setType(curType);
        advance();
        if (ch == nextCh) {
            val = val + (char) ch;
            advance();
            token.setType(nextType);
        }
        token.setValue(val);
        return token;
    }

    // IDENTIFIER: Letter LetterOrDigit*;
    // Letter: [a-zA-Z$_];
    // LetterOrDigit
    //    : Letter
    //    | [0-9]
    //    ;
    private Token identifier() {
        Token token = new Token(line, column);
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        advance();
        while (Character.isLetterOrDigit(ch) || ch == '$' || ch == '_') {
            builder.append((char) ch);
            advance();
        }

        String val = builder.toString();
        token.setType(Token.IDENTIFIER);
        if (keywords.contains(val)) {
            token.setType(Token.KEY_WORD);
        }
        token.setValue(val);
        return token;
    }

    // DECIMAL_LITERAL:    ('0' | [1-9] (Digits? | '_'+ Digits)) [lL]?;
    // HEX_LITERAL:        '0' [xX] [0-9a-fA-F] ([0-9a-fA-F_]* [0-9a-fA-F])? [lL]?;
    // OCT_LITERAL:        '0' '_'* [0-7] ([0-7_]* [0-7])? [lL]?;
    // BINARY_LITERAL:     '0' [bB] [01] ([01_]* [01])? [lL]?;
    //
    // FLOAT_LITERAL:      (Digits '.' Digits? | '.' Digits) ExponentPart? [fFdD]?
    //              |       Digits (ExponentPart [fFdD]? | [fFdD])
    //              ;
    //
    // HEX_FLOAT_LITERAL:  '0' [xX] (HexDigits '.'? | HexDigits? '.' HexDigits) [pP] [+-]? Digits [fFdD]?;
    // fragment Digits
    //    : [0-9] ([0-9_]* [0-9])?
    //    ;
    // fragment ExponentPart
    //    : [eE] [+-]? Digits
    //    ;
    private Token number() {
        Token token = new Token(line, column);
//        if (ch == '0') {
//            advance();
//            if (ch == 'x' || ch == 'X') {
//                // HEX_LITERAL
//                // HEX_FLOAT_LITERAL
//            }
//            if (ch == 'b' || ch == 'B') {
//                // BINARY_LITERAL
//            }
//        }
        StringBuilder builder = new StringBuilder();
        while (Character.isDigit(ch)) {
            builder.append((char) ch);
            advance();
        }
        token.setType(Token.NUMBER);
        token.setValue(builder.toString());
        return token;
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
}
