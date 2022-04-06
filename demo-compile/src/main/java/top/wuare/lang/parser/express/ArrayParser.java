package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.ArrayExpr;
import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.lexer.TokenType;
import top.wuare.lang.parser.Parser;

import java.util.List;

public class ArrayParser implements PrefixParser {

    @Override
    public Expr parse(Parser parser, Token token) {
        parser.consume();
        ArrayExpr arrayExpr = new ArrayExpr();
        List<Expr> items = arrayExpr.getItems();
        if (parser.getCurToken() != null && parser.getCurToken().getType() != TokenType.RBRACKET) {
            Expr expr = parser.parseExp(0);
            items.add(expr);
            while (parser.getCurToken() != null && parser.getCurToken().getType() == TokenType.COMMA) {
                parser.consume();
                items.add(parser.parseExp(0));
            }
        }
        parser.want(TokenType.RBRACKET);
        parser.consume();
        return arrayExpr;
    }
}
