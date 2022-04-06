package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.expr.Expr;

public class ForStmt implements Stmt {
    private Stmt initStmt;
    private Expr expr;
    private Expr updateExpr;
    private Block block;

    public Stmt getInitStmt() {
        return initStmt;
    }

    public void setInitStmt(Stmt initStmt) {
        this.initStmt = initStmt;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public Expr getUpdateExpr() {
        return updateExpr;
    }

    public void setUpdateExpr(Expr updateExpr) {
        this.updateExpr = updateExpr;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "ForStmt{" +
                "initStmt=" + initStmt +
                ", expr=" + expr +
                ", updateExpr=" + updateExpr +
                ", block=" + block +
                '}';
    }
}
