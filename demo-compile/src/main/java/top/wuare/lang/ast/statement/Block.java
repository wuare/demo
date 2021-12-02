package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.AST;

import java.util.List;

public class Block implements AST {

    public Block(List<Stmt> stmts) {
        this.stmts = stmts;
    }

    private List<Stmt> stmts;

    public List<Stmt> getStmts() {
        return stmts;
    }

    public void setStmts(List<Stmt> stmts) {
        this.stmts = stmts;
    }
}
