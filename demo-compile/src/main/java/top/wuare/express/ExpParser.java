package top.wuare.express;

import top.wuare.express.ast.Expr;
import top.wuare.express.parser.*;

import java.util.HashMap;
import java.util.Map;

public class ExpParser {

    private final ExpLexer lexer;
    private ExpToken curToken;
    private final Map<ExpTokenType, PrefixParseLet> mPrefixParseLets = new HashMap<>();
    private final Map<ExpTokenType, InfixParseLet> mInfixParseLets = new HashMap<>();


    public ExpParser(String text) {
        this.lexer = new ExpLexer(text);
        init();
        consume();
    }

    private void init() {
        register(ExpTokenType.NUMBER, new NameParseLet());
        register(ExpTokenType.IDENT, new NameParseLet());
        register(ExpTokenType.SUB, new PrefixOperatorParseLet());
        register(ExpTokenType.BANG, new PrefixOperatorParseLet());
        register(ExpTokenType.LPAREN, new ParenParseLet());

        // infix
        register(ExpTokenType.ADD, new BinaryOperatorParseLet(10));
        register(ExpTokenType.SUB, new BinaryOperatorParseLet(10));
        register(ExpTokenType.MUL, new BinaryOperatorParseLet(11));
        register(ExpTokenType.DIV, new BinaryOperatorParseLet(11));
        register(ExpTokenType.MOD, new BinaryOperatorParseLet(11));
        register(ExpTokenType.LT, new BinaryOperatorParseLet(3));
        register(ExpTokenType.GT, new BinaryOperatorParseLet(3));
        register(ExpTokenType.LE, new BinaryOperatorParseLet(3));
        register(ExpTokenType.GE, new BinaryOperatorParseLet(3));
        register(ExpTokenType.EQUAL, new BinaryOperatorParseLet(3));
        register(ExpTokenType.NOTEQUAL, new BinaryOperatorParseLet(3));
        register(ExpTokenType.AND, new BinaryOperatorParseLet(2));
        register(ExpTokenType.OR, new BinaryOperatorParseLet(1));
    }

    public Expr parseExp(int precedence) {
        if (curToken == null) {
            return null;
        }
        PrefixParseLet prefixParseLet = mPrefixParseLets.get(curToken.getType());
        if (prefixParseLet == null) {
            throw new RuntimeException("could not parse '" + curToken.getType().getText() + "'");
        }
        Expr left = prefixParseLet.parse(this, curToken);
        while (precedence < getPrecedence()) {
            ExpToken token = curToken;
            InfixParseLet infixParseLet = mInfixParseLets.get(curToken.getType());
            if (infixParseLet == null) {
                return left;
            }
            consume();
            left = infixParseLet.parse(this, left, token);
        }
        return left;
    }

    private int getPrecedence() {
        if (curToken == null) {
            return 0;
        }
        InfixParseLet infixParseLet = mInfixParseLets.get(curToken.getType());
        if (infixParseLet != null) {
            return infixParseLet.getPrecedence();
        }
        return 0;
    }

    public void consume() {
        curToken = lexer.nextToken();
    }

    public void want(ExpTokenType type) {
        if (type == curToken.getType()) {
            return;
        }
        throw new RuntimeException("expect token type: " + type.getText() + ", but get type: "
                + curToken.getType().getText() + ", at line: " + curToken.getLine() + ", column: " + curToken.getColumn());
    }

    private void eat(ExpTokenType type) {
        if (type == curToken.getType()) {
            consume();
            return;
        }
        throw new RuntimeException("expect token type: " + type.getText() + ", but get type: "
                + curToken.getType().getText() + ", at line: " + curToken.getLine() + ", column: " + curToken.getColumn());
    }

    public void register(ExpTokenType type, PrefixParseLet prefixParseLet) {
        mPrefixParseLets.put(type, prefixParseLet);
    }

    public void register(ExpTokenType type, InfixParseLet infixParseLet) {
        mInfixParseLets.put(type, infixParseLet);
    }

    public ExpToken getCurToken() {
        return curToken;
    }
}
