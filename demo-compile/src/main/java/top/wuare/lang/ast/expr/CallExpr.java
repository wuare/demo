package top.wuare.lang.ast.expr;

import top.wuare.lang.lexer.Token;

import java.util.List;

public class CallExpr implements Expr {
    private Token name;
    private List<Expr> args;

    public Token getName() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }

    public List<Expr> getArgs() {
        return args;
    }

    public void setArgs(List<Expr> args) {
        this.args = args;
    }
}
