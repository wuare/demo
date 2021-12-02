package top.wuare.lang.ast.expr;

import top.wuare.lang.lexer.Token;

public class OperatorExpr implements Expr {
    private Token token;
    private Expr left;
    private Expr right;

    public OperatorExpr(Token token, Expr left, Expr right) {
        this.token = token;
        this.left = left;
        this.right = right;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Expr getLeft() {
        return left;
    }

    public void setLeft(Expr left) {
        this.left = left;
    }

    public Expr getRight() {
        return right;
    }

    public void setRight(Expr right) {
        this.right = right;
    }
}
