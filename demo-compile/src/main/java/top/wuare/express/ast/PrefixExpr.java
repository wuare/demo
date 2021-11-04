package top.wuare.express.ast;

import top.wuare.express.ExpTokenType;

public class PrefixExpr implements Expr {

    private ExpTokenType tokenType;
    private Expr operand;

    public PrefixExpr(ExpTokenType tokenType, Expr operand) {
        this.tokenType = tokenType;
        this.operand = operand;
    }

    public ExpTokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(ExpTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Expr getOperand() {
        return operand;
    }

    public void setOperand(Expr operand) {
        this.operand = operand;
    }
}
