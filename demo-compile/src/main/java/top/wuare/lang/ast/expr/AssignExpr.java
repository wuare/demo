package top.wuare.lang.ast.expr;

import top.wuare.lang.lexer.Token;

public class AssignExpr implements Expr {
    private Token token;
    private Expr identExpr;
    private Expr expr;

    public AssignExpr(Token token, Expr identExpr, Expr expr) {
        this.token = token;
        this.identExpr = identExpr;
        this.expr = expr;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Expr getIdentExpr() {
        return identExpr;
    }

    public void setIdentExpr(Expr identExpr) {
        this.identExpr = identExpr;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }
}
