package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.ast.expr.PrefixExpr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.parser.Parser;

public class PrefixOperatorParser implements PrefixParser {

    private final int precedence;

    public PrefixOperatorParser(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expr parse(Parser parser, Token token) {

        parser.consume();
        Expr operand = parser.parseExp(precedence);
        return new PrefixExpr(token.getType(), operand);
    }
}
