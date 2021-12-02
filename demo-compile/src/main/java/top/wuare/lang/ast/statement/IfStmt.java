package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.expr.Expr;

public class IfStmt implements Stmt {
    private Expr expr;
    private Block then;
    private Block els;

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public Block getThen() {
        return then;
    }

    public void setThen(Block then) {
        this.then = then;
    }

    public Block getEls() {
        return els;
    }

    public void setEls(Block els) {
        this.els = els;
    }
}
