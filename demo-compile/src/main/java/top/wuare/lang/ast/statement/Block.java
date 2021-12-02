package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.AST;

import java.util.List;

public class Block implements AST {
    private List<Stmt> statements;

    public List<Stmt> getStatements() {
        return statements;
    }

    public void setStatements(List<Stmt> statements) {
        this.statements = statements;
    }
}
