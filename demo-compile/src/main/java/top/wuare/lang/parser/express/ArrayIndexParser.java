package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.ArrayIndexExpr;
import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.lexer.TokenType;
import top.wuare.lang.parser.Parser;

public class ArrayIndexParser implements InfixParser {

    private final int precedence;

    public ArrayIndexParser(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        Expr expr = parser.parseExp(0);
        parser.want(TokenType.RBRACKET);
        parser.consume(); // consume ']'
        return new ArrayIndexExpr(token, left, expr);
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }
}
