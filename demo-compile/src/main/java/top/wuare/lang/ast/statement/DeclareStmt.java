package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.lexer.Token;

public class DeclareStmt implements Stmt {
    private Token ident;
    private Expr expr;

    public Token getIdent() {
        return ident;
    }

    public void setIdent(Token ident) {
        this.ident = ident;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }
}
