package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.expr.Expr;

public class ExprStmt implements Stmt {
    private Expr expr;

    public ExprStmt(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }
}
