package top.wuare.lang.ast.statement;

import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class FuncDeclareStmt implements Stmt {
    private Token name;
    private List<Expr> args;
    private Block block;

    public Token getName() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }

    public List<Expr> getArgs() {
        return args == null ? new ArrayList<>() : args;
    }

    public void setArgs(List<Expr> args) {
        this.args = args;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
