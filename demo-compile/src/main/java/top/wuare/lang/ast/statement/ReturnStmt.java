package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.expr.Expr;

public class ReturnStmt implements Stmt {
    private Expr expr;

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }
}
