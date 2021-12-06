package top.wuare.lang.ast.expr;


import top.wuare.lang.lexer.Token;

public class PrefixExpr implements Expr {

    private Token token;
    private Expr operand;

    public PrefixExpr(Token token, Expr operand) {
        this.token = token;
        this.operand = operand;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Expr getOperand() {
        return operand;
    }

    public void setOperand(Expr operand) {
        this.operand = operand;
    }
}
