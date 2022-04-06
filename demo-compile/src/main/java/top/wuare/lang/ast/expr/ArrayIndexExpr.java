package top.wuare.lang.ast.expr;

import top.wuare.lang.lexer.Token;

public class ArrayIndexExpr implements Expr {

    private Token token;
    private Expr expr;
    private Expr indexExpr;

    public ArrayIndexExpr(Token token, Expr expr, Expr indexExpr) {
        this.token = token;
        this.expr = expr;
        this.indexExpr = indexExpr;
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

    public Expr getIndexExpr() {
        return indexExpr;
    }

    public void setIndexExpr(Expr indexExpr) {
        this.indexExpr = indexExpr;
    }
}
