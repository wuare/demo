package top.wuare.lang.parser;

import org.junit.Assert;
import org.junit.Test;
import top.wuare.lang.ast.AST;
import top.wuare.lang.ast.statement.Block;
import top.wuare.lang.ast.statement.VarDeclareStmt;
import top.wuare.lang.ast.statement.Stmt;

import java.util.List;

public class ParserTest {

    @Test
    public void testVarDeclareStmt() {
        Parser parser = new Parser("var a = 1;");
        AST ast = parser.parse();
        Assert.assertNotNull(ast);
        Assert.assertTrue(ast instanceof Block);
        Block block = (Block) ast;
        List<Stmt> stmts = block.getStmts();
        Assert.assertTrue(stmts != null && stmts.size() == 1);
        Assert.assertTrue(stmts.get(0) instanceof VarDeclareStmt);
    }

    @Test
    public void testIfStmt() {
        Parser parser = new Parser("if (a) { var b = 1; } else { var b = 1; }");
        AST ast = parser.parse();
        System.out.println(ast);
    }

    @Test
    public void testWhileStmt() {
        String whileText = "while (a) { }";
        Parser parser = new Parser(whileText);
        AST ast = parser.parse();
        System.out.println(ast);
    }

    @Test
    public void testForStmt() {
        String forText = "for (a = 2; a < 3; a = a + 1) { print(a);}";
        Parser parser = new Parser(forText);
        AST ast = parser.parse();
        System.out.println(ast);
    }

    @Test
    public void testCallExpr() {
        String whileText = "a(b,c);";
        Parser parser = new Parser(whileText);
        AST ast = parser.parse();
        System.out.println(ast);
    }

    @Test
    public void testFuncDeclareStmt() {
        String funcStr = "func a(b,c) {return;}";
        Parser parser = new Parser(funcStr);
        AST ast = parser.parse();
        System.out.println(ast);
    }

    @Test
    public void testBlock() {
        String text = "var a;                   \n" +
                "      var b = 1;               \n" +
                "      func c(a,b) {            \n" +
                "          if (a == b) {        \n" +
                "              return true;     \n" +
                "          } else {             \n" +
                "              return false;    \n" +
                "          }                      " +
                "      }                          " +
                "      c(1,1);                    ";
        Parser parser = new Parser(text);
        AST ast = parser.parse();
        System.out.println(ast);
    }

    @Test
    public void testArray() {
        String code = "[1, 2, 3]";
        Parser parser = new Parser(code);
        AST ast = parser.parseExp(0);
        System.out.println(ast);
    }

    @Test
    public void testArrayIndex() {
        String code = "a[1]";
        Parser parser = new Parser(code);
        AST ast = parser.parseExp(0);
        System.out.println(ast);
    }
    @Test
    public void testArrayAssign() {
        String code = "[1][0] = 1;";
        Parser parser = new Parser(code);
        AST ast = parser.parse();
        System.out.println(ast);
    }
}
