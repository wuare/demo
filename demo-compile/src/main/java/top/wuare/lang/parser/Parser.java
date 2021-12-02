package top.wuare.lang.parser;

import top.wuare.lang.ast.AST;
import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.lexer.Lexer;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.lexer.TokenType;
import top.wuare.lang.parser.express.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private final Lexer lexer;

    private Token curToken;
    private static final Map<TokenType, PrefixParser> mPrefixParseLets = new HashMap<>();
    private static final Map<TokenType, InfixParser> mInfixParseLets = new HashMap<>();

    public Parser(String text) {
        this.lexer = new Lexer(text);
        consume();
    }

    public Parser(InputStream in) {
        this.lexer = new Lexer(in);
        consume();
    }

    public AST parse() {
        return null;
    }


    public Expr parseExp(int precedence) {
        if (curToken == null) {
            return null;
        }
        PrefixParser prefixParser = mPrefixParseLets.get(curToken.getType());
        if (prefixParser == null) {
            throw new RuntimeException("could not parse '" + curToken.getType().getText() + "'");
        }
        Expr left = prefixParser.parse(this, curToken);
        while (precedence < getPrecedence()) {
            Token token = curToken;
            InfixParser infixParser = mInfixParseLets.get(curToken.getType());
            if (infixParser == null) {
                return left;
            }
            consume();
            left = infixParser.parse(this, left, token);
        }
        return left;
    }

    private int getPrecedence() {
        if (curToken == null) {
            return 0;
        }
        InfixParser infixParser = mInfixParseLets.get(curToken.getType());
        if (infixParser != null) {
            return infixParser.getPrecedence();
        }
        return 0;
    }

    public void consume() {
        curToken = lexer.nextToken();
    }

    public void want(TokenType type) {
        if (type == curToken.getType()) {
            return;
        }
        throw new RuntimeException("expect token type: " + type.getText() + ", but get type: "
                + curToken.getType().getText() + ", at line: " + curToken.getLine() + ", column: " + curToken.getColumn());
    }

    private void eat(TokenType type) {
        if (type == curToken.getType()) {
            consume();
            return;
        }
        throw new RuntimeException("expect token type: " + type.getText() + ", but get type: "
                + curToken.getType().getText() + ", at line: " + curToken.getLine() + ", column: " + curToken.getColumn());
    }

    static {
        register(TokenType.NUMBER, new IdentParser());
        register(TokenType.IDENT, new IdentParser());
        register(TokenType.SUB, new PrefixOperatorParser(12));
        register(TokenType.BANG, new PrefixOperatorParser(12));
        register(TokenType.LPAREN, new ParenParser());

        // infix
        register(TokenType.ADD, new BinOperatorParser(10));
        register(TokenType.SUB, new BinOperatorParser(10));
        register(TokenType.MUL, new BinOperatorParser(11));
        register(TokenType.DIV, new BinOperatorParser(11));
        register(TokenType.MOD, new BinOperatorParser(11));
        register(TokenType.LT, new BinOperatorParser(3));
        register(TokenType.GT, new BinOperatorParser(3));
        register(TokenType.LE, new BinOperatorParser(3));
        register(TokenType.GE, new BinOperatorParser(3));
        register(TokenType.EQUAL, new BinOperatorParser(3));
        register(TokenType.NOTEQUAL, new BinOperatorParser(3));
        register(TokenType.AND, new BinOperatorParser(2));
        register(TokenType.OR, new BinOperatorParser(1));
    }

    public static void register(TokenType type, PrefixParser prefixParser) {
        mPrefixParseLets.put(type, prefixParser);
    }

    public static void register(TokenType type, InfixParser infixParser) {
        mInfixParseLets.put(type, infixParser);
    }
}
