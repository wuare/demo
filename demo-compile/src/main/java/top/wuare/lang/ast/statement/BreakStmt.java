package top.wuare.lang.ast.statement;

import top.wuare.lang.lexer.Token;

public class BreakStmt implements Stmt {
    private Token token;

    public BreakStmt(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
