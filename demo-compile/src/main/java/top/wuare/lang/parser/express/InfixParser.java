package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.parser.Parser;

public interface InfixParser {
    Expr parse(Parser parser, Expr left, Token token);

    int getPrecedence();
}
