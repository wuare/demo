package top.wuare.lang.ast.expr;


import top.wuare.lang.lexer.TokenType;

public class PrefixExpr implements Expr {

    private TokenType tokenType;
    private Expr operand;

    public PrefixExpr(TokenType tokenType, Expr operand) {
        this.tokenType = tokenType;
        this.operand = operand;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Expr getOperand() {
        return operand;
    }

    public void setOperand(Expr operand) {
        this.operand = operand;
    }
}
