package top.wuare.lang.ast.expr;

import top.wuare.lang.lexer.Token;

public class IdentExpr implements Expr {

    private final Token token;

    public IdentExpr(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
