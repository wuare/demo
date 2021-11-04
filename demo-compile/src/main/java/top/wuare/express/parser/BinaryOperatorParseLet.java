package top.wuare.express.parser;

import top.wuare.express.ExpParser;
import top.wuare.express.ExpToken;
import top.wuare.express.ast.Expr;
import top.wuare.express.ast.OperatorExpr;

public class BinaryOperatorParseLet implements InfixParseLet {

    private final int precedence;

    public BinaryOperatorParseLet(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expr parse(ExpParser parser, Expr left, ExpToken token) {
        Expr right = parser.parseExp(precedence);
        return new OperatorExpr(token, token.getType(), left, right);
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }
}
