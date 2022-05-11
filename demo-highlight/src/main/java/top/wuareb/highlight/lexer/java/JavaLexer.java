package top.wuareb.highlight.lexer.java;

import top.wuareb.highlight.exception.LexerException;
import top.wuareb.highlight.util.Sets;

import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

public class JavaLexer {
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

    public JavaLexer(String text) {
        reader = new StringReader(text);
        advance();
    }

    public JavaToken nextToken() {
        if (ch == -1) {
            return null;
        }
        // skip while space
        if (Character.isWhitespace(ch)) {
            StringBuilder spaceBuilder = new StringBuilder();
            JavaToken spaceToken = new JavaToken(line, column);
            while (Character.isWhitespace(ch)) {
                spaceBuilder.append((char) ch);
                advance();
            }
            spaceToken.setType(JavaToken.WHITE_SPACE);
            spaceToken.setValue(spaceBuilder.toString());
            // 特定场景，高亮需保存空白符，如果是编译构造语法树，空白符不保留
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
                JavaToken token3 = new JavaToken(JavaToken.LPAREN, "(", line, column);
                advance();
                return token3;
            case ')':
                JavaToken token4 = new JavaToken(JavaToken.RPAREN, ")", line, column);
                advance();
                return token4;
            case '{':
                JavaToken token5 = new JavaToken(JavaToken.LBRACE, "{", line, column);
                advance();
                return token5;
            case '}':
                JavaToken token6 = new JavaToken(JavaToken.RBRACE, "}", line, column);
                advance();
                return token6;
            case '[':
                JavaToken token7 = new JavaToken(JavaToken.LBRACKET, "[", line, column);
                advance();
                return token7;
            case ']':
                JavaToken token8 = new JavaToken(JavaToken.RBRACKET, "]", line, column);
                advance();
                return token8;
            case ';':
                JavaToken token9 = new JavaToken(JavaToken.SEMI, ";", line, column);
                advance();
                return token9;
            case ',':
                JavaToken token10 = new JavaToken(JavaToken.COMMA, ",", line, column);
                advance();
                return token10;
            case '.':
                JavaToken token11 = new JavaToken(JavaToken.DOT, ".", line, column);
                advance();
                return token11;
            case '=':
                // '=', '=='
                return assign_2('=', JavaToken.ASSIGN, JavaToken.EQUAL);
            case '>':
                JavaToken token12 = new JavaToken(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    token12.setType(JavaToken.GE);
                    token12.setValue(">=");
                    return token12;
                }
                if (ch == '>') {
                    advance();
                    if (ch == '>') {
                        advance();
                        if (ch == '=') {
                            advance();
                            token12.setType(JavaToken.U_R_SHIFT_ASSIGN);
                            token12.setValue(">>>=");
                            return token12;
                        }
                        token12.setType(JavaToken.U_R_SHIFT);
                        token12.setValue(">>>");
                        return token12;
                    }
                    if (ch == '=') {
                        advance();
                        token12.setType(JavaToken.R_SHIFT_ASSIGN);
                        token12.setValue(">>=");
                        return token12;
                    }
                    token12.setType(JavaToken.R_SHIFT);
                    token12.setValue(">>");
                    return token12;
                }
                token12.setType(JavaToken.GT);
                token12.setValue(">");
                return token12;
            case '<':
                JavaToken token13 = new JavaToken(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    token13.setType(JavaToken.LE);
                    token13.setValue("<=");
                    return token13;
                }
                if (ch == '<') {
                    advance();
                    if (ch == '=') {
                        advance();
                        token13.setType(JavaToken.L_SHIFT_ASSIGN);
                        token13.setValue("<<=");
                        return token13;
                    }
                    token13.setType(JavaToken.L_SHIFT);
                    token13.setValue("<<");
                    return token13;
                }
                token13.setType(JavaToken.LT);
                token13.setValue("<");
                return token13;
            case '!':
                JavaToken token14 = new JavaToken(line, column);
                advance();
                if (ch == '=') {
                    advance();
                    token14.setType(JavaToken.NOTEQUAL);
                    token14.setValue("!=");
                    return token14;
                }
                token14.setType(JavaToken.BANG);
                token14.setValue("!");
                return token14;
            case '~':
                JavaToken token15 = new JavaToken(JavaToken.TILDE, "~", line, column);
                advance();
                return token15;
            case '?':
                JavaToken token16 = new JavaToken(JavaToken.QUESTION, "?", line, column);
                advance();
                return token16;
            case ':':
                JavaToken token17 = new JavaToken(JavaToken.COLON, ":", line, column);
                advance();
                return token17;
            case '&':
                JavaToken token18 = new JavaToken(line, column);
                advance();
                if (ch == '&') {
                    advance();
                    token18.setType(JavaToken.AND);
                    token18.setValue("&&");
                    return token18;
                }
                if (ch == '=') {
                    advance();
                    token18.setType(JavaToken.AND_ASSIGN);
                    token18.setValue("&=");
                    return token18;
                }
                token18.setType(JavaToken.BIT_AND);
                token18.setValue("&");
                return token18;
            case '|':
                JavaToken token19 = new JavaToken(line, column);
                advance();
                if (ch == '|') {
                    advance();
                    token19.setType(JavaToken.OR);
                    token19.setValue("||");
                    return token19;
                }
                if (ch == '=') {
                    advance();
                    token19.setType(JavaToken.OR_ASSIGN);
                    token19.setValue("|=");
                    return token19;
                }
                token19.setType(JavaToken.BIT_OR);
                token19.setValue("|");
                return token19;
            case '+':
                JavaToken token20 = new JavaToken(line, column);
                advance();
                if (ch == '+') {
                    advance();
                    token20.setType(JavaToken.INC);
                    token20.setValue("++");
                    return token20;
                }
                if (ch == '=') {
                    advance();
                    token20.setType(JavaToken.ADD_ASSIGN);
                    token20.setValue("+=");
                    return token20;
                }
                token20.setType(JavaToken.ADD);
                token20.setValue("+");
                return token20;
            case '-':
                JavaToken token21 = new JavaToken(line, column);
                advance();
                if (ch == '-') {
                    advance();
                    token21.setType(JavaToken.DEC);
                    token21.setValue("--");
                    return token21;
                }
                if (ch == '=') {
                    advance();
                    token21.setType(JavaToken.SUB_ASSIGN);
                    token21.setValue("-=");
                    return token21;
                }
                token21.setType(JavaToken.SUB);
                token21.setValue("-");
                return token21;
            case '*':
                return assign_2('=', JavaToken.MUL, JavaToken.MUL_ASSIGN);
            case '^':
                return assign_2('=', JavaToken.CARET, JavaToken.XOR_ASSIGN);
            case '%':
                return assign_2('=', JavaToken.MOD, JavaToken.MOD_ASSIGN);
            case '@':
                JavaToken token22 = new JavaToken(JavaToken.AT, "@", line, column);
                advance();
                return token22;
            default:
//                throw new LexerException("syntax error at line: " + line + ", column " + column + ", unexpect character: '" + (char) ch + "'");
                JavaToken token23 = new JavaToken(JavaToken.UN_KNOW, String.valueOf((char) ch), line, column);
                advance();
                return token23;
        }
    }

    // COMMENT:            '/*' .*? '*/'    -> channel(HIDDEN);
    // LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);
    private JavaToken comment() {
        StringBuilder builder = new StringBuilder();
        JavaToken token = new JavaToken(line, column);
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
            token.setType(JavaToken.COMMENT);
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
            token.setType(JavaToken.LINE_COMMENT);
            token.setValue(builder.toString());
            return token;
        }
        if (ch == '=') {
            token.setType(JavaToken.DIV_ASSIGN);
            token.setValue("/=");
            return token;
        }
        token.setType(JavaToken.DIV);
        token.setValue("/");
        return token;
    }

    // fragment EscapeSequence
    //    : '\\' [btnfr"'\\]
    //    | '\\' ([0-3]? [0-7])? [0-7]
    //    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
    //    ;

    // CHAR_LITERAL:       '\'' (~['\\\r\n] | EscapeSequence) '\'';
    private JavaToken charLiteral() {
        StringBuilder builder = new StringBuilder();
        JavaToken token = new JavaToken(line, column);
        advance();
        if (ch == '\'') {
            token.setType(JavaToken.CHAR_LITERAL);
            token.setValue(builder.toString());
            advance();
            return token;
        }
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
        token.setType(JavaToken.CHAR_LITERAL);
        token.setValue(builder.toString());
        advance();
        return token;
    }

    // STRING_LITERAL:     '"' (~["\\\r\n] | EscapeSequence)* '"';
    private JavaToken stringLiteral() {
        StringBuilder builder = new StringBuilder();
        JavaToken token = new JavaToken(line, column);
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
        token.setType(JavaToken.STRING_LITERAL);
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

    private JavaToken assign_2(int nextCh, int curType, int nextType) {
        JavaToken token = new JavaToken(line, column);
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
    private JavaToken identifier() {
        JavaToken token = new JavaToken(line, column);
        StringBuilder builder = new StringBuilder();
        builder.append((char) ch);
        advance();
        while (Character.isLetterOrDigit(ch) || ch == '$' || ch == '_') {
            builder.append((char) ch);
            advance();
        }

        String val = builder.toString();
        token.setType(JavaToken.IDENTIFIER);
        if (keywords.contains(val)) {
            token.setType(JavaToken.KEY_WORD);
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
    private JavaToken number() {
        JavaToken token = new JavaToken(line, column);
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
        while (Character.isDigit(ch) || ch == '.') {
            builder.append((char) ch);
            advance();
        }
        token.setType(JavaToken.NUMBER);
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
