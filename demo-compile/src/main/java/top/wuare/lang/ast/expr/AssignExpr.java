package top.wuare.lang.ast.expr;

import top.wuare.lang.lexer.Token;

public class AssignExpr implements Expr {
    private Token token;
    private Expr expr;

    public AssignExpr(Token token, Expr expr) {
        this.token = token;
        this.expr = expr;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }
}
