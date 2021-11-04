package top.wuare.express.ast;

import top.wuare.express.ExpToken;
import top.wuare.express.ExpTokenType;

public class OperatorExpr implements Expr {

    private ExpToken token;
    private ExpTokenType type;
    private Expr left;
    private Expr right;

    public OperatorExpr(ExpToken token, ExpTokenType type, Expr left, Expr right) {
        this.token = token;
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public ExpToken getToken() {
        return token;
    }

    public void setToken(ExpToken token) {
        this.token = token;
    }

    public ExpTokenType getType() {
        return type;
    }

    public void setType(ExpTokenType type) {
        this.type = type;
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
