package top.wuare.express.parser;

import top.wuare.express.ExpParser;
import top.wuare.express.ExpToken;
import top.wuare.express.ast.Expr;
import top.wuare.express.ast.PrefixExpr;

public class ParenParseLet implements PrefixParseLet {

    @Override
    public Expr parse(ExpParser parser, ExpToken token) {
        parser.consume();
        Expr expr = parser.parseExp(0);
        parser.consume();
        return new PrefixExpr(token.getType(), expr);
    }
}
