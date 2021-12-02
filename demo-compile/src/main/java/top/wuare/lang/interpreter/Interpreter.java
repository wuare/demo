package top.wuare.lang.interpreter;

import top.wuare.lang.ast.AST;
import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.ast.statement.Block;
import top.wuare.lang.ast.statement.Stmt;

public class Interpreter {

    public Object eval(AST ast) {
        if (ast instanceof Expr) {
            return evalExpr((Expr) ast);
        }
        if (ast instanceof Block) {
            return evalBlock((Block) ast);
        }
        if (ast instanceof Stmt) {
            return evalStmt((Stmt) ast);
        }
        throw new RuntimeException("Unknown AST");
    }

    private Object evalExpr(Expr ast) {
        return null;
    }

    private Object evalBlock(Block ast) {
        return null;
    }

    private Object evalStmt(Stmt ast) {
        return null;
    }
}
