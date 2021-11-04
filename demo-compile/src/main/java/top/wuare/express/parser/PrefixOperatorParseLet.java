package top.wuare.express.parser;

import top.wuare.express.ExpParser;
import top.wuare.express.ExpToken;
import top.wuare.express.ast.Expr;
import top.wuare.express.ast.PrefixExpr;

public class PrefixOperatorParseLet implements PrefixParseLet {

    private final int precedence;

    public PrefixOperatorParseLet(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expr parse(ExpParser parser, ExpToken token) {
        parser.consume();
        Expr operand = parser.parseExp(precedence);
        return new PrefixExpr(token.getType(), operand);
    }
}
