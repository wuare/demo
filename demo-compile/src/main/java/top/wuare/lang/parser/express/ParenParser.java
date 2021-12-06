package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.ast.expr.PrefixExpr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.parser.Parser;

public class ParenParser implements PrefixParser {

    @Override
    public Expr parse(Parser parser, Token token) {
        parser.consume();
        Expr expr = parser.parseExp(0);
        parser.consume();
        return new PrefixExpr(token, expr);
    }
}
