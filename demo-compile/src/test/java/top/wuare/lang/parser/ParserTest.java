package top.wuare.lang.parser;

import org.junit.Assert;
import org.junit.Test;
import top.wuare.lang.ast.AST;
import top.wuare.lang.ast.statement.Block;
import top.wuare.lang.ast.statement.DeclareStmt;
import top.wuare.lang.ast.statement.Stmt;

import java.util.List;

public class ParserTest {

    @Test
    public void testDeclareStmt() {
        Parser parser = new Parser("var a = 1;");
        AST ast = parser.parse();
        Assert.assertNotNull(ast);
        Assert.assertTrue(ast instanceof Block);
        Block block = (Block) ast;
        List<Stmt> stmts = block.getStmts();
        Assert.assertTrue(stmts != null && stmts.size() == 1);
        Assert.assertTrue(stmts.get(0) instanceof DeclareStmt);
    }

    @Test
    public void testIfStmt() {
        Parser parser = new Parser("if (a) { var b = 1; } else { var b = 1; }");
        AST ast = parser.parse();
        System.out.println(ast);
    }
}
