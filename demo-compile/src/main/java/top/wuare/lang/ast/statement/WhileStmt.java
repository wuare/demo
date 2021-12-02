package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.expr.Expr;

public class WhileStmt implements Stmt {
    private Expr expr;
    private Block block;

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
