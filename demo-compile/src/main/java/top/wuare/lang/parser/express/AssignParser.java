package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.AssignExpr;
import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.ast.expr.IdentExpr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.parser.Parser;

public class AssignParser implements InfixParser {
    private final int precedence;

    public AssignParser(int precedence) {
        this.precedence = precedence;
    }
    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        IdentExpr ident = (IdentExpr) left;
        Expr expr = parser.parseExp(precedence);
        return new AssignExpr(ident.getToken(), expr);
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }
}
