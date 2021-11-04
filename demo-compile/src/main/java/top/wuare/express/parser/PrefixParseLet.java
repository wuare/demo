package top.wuare.express.parser;

import top.wuare.express.ExpParser;
import top.wuare.express.ExpToken;
import top.wuare.express.ast.Expr;

public interface PrefixParseLet {
    Expr parse(ExpParser parser, ExpToken token);
}
