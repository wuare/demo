package top.wuareb.highlight.lexer.java;

public class JavaToken {

    public static final int IDENTIFIER = 1;
    public static final int NUMBER = 2;
    public static final int LPAREN = 3;
    public static final int RPAREN = 4;
    public static final int LBRACE = 5;
    public static final int RBRACE = 6;
    public static final int LBRACKET = 7;
    public static final int RBRACKET = 8;
    public static final int SEMI = 9;
    public static final int COMMA = 10;
    public static final int DOT = 11;

    public static final int ASSIGN = 12;
    public static final int GT = 13;
    public static final int LT = 14;
    public static final int BANG = 15;
    public static final int TILDE = 16;
    public static final int QUESTION = 17;
    public static final int COLON = 18;
    public static final int EQUAL = 19;
    public static final int LE = 20;
    public static final int GE = 21;
    public static final int NOTEQUAL = 22;
    public static final int AND = 23;
    public static final int OR = 24;
    public static final int INC = 25;
    public static final int DEC = 26;
    public static final int ADD = 27;
    public static final int SUB = 28;
    public static final int MUL = 29;
    public static final int DIV = 30;
    public static final int BIT_AND = 31;
    public static final int BIT_OR = 32;
    public static final int CARET = 33;
    public static final int MOD = 34;
    public static final int ADD_ASSIGN = 35;
    public static final int SUB_ASSIGN = 36;
    public static final int MUL_ASSIGN = 37;
    public static final int DIV_ASSIGN = 38;
    public static final int AND_ASSIGN = 39;
    public static final int OR_ASSIGN = 40;
    public static final int XOR_ASSIGN = 41;
    public static final int MOD_ASSIGN = 42;
    public static final int L_SHIFT_ASSIGN = 43;
    public static final int R_SHIFT_ASSIGN = 44;
    public static final int U_R_SHIFT_ASSIGN = 45;

    public static final int R_SHIFT = 46;
    public static final int U_R_SHIFT = 47;
    public static final int L_SHIFT = 48;
    public static final int CHAR_LITERAL = 49;
    public static final int STRING_LITERAL = 50;

    public static final int KEY_WORD = 51;
    public static final int LINE_COMMENT = 52;
    public static final int COMMENT = 53;

    public static final int WHITE_SPACE = 54;
    public static final int AT = 55;
    public static final int UN_KNOW = 99;

    private int type;
    private String value;

    private int line;
    private int column;

    public JavaToken(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public JavaToken(int type, String value, int line, int column) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
