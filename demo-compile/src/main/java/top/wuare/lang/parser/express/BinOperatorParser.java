package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.ast.expr.OperatorExpr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.parser.Parser;

public class BinOperatorParser implements InfixParser {

    private final int precedence;

    public BinOperatorParser(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        Expr right = parser.parseExp(precedence);
        return new OperatorExpr(token, left, right);
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }
}
