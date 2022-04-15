package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.lexer.Token;

public class ForEachStmt implements Stmt {
    private Token token;
    private Expr expr;
    private Block block;

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

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
