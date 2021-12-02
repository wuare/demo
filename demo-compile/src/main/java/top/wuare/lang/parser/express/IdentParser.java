package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.ast.expr.IdentExpr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.parser.Parser;

public class IdentParser implements PrefixParser {

    @Override
    public Expr parse(Parser parser, Token token) {
        IdentExpr expr = new IdentExpr(token);
        parser.consume();
        return expr;
    }
}
