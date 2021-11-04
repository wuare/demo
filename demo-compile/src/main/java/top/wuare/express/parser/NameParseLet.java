package top.wuare.express.parser;

import top.wuare.express.ExpParser;
import top.wuare.express.ExpToken;
import top.wuare.express.ast.Expr;
import top.wuare.express.ast.NameExpr;

public class NameParseLet implements PrefixParseLet {

    @Override
    public Expr parse(ExpParser parser, ExpToken token) {
        NameExpr nameExpr = new NameExpr(token.getText());
        parser.consume();
        return nameExpr;
    }
}
