package top.wuare.express;

import org.junit.Test;
import top.wuare.express.ast.Expr;

public class ExpParserTest {

    @Test
    public void testParseExpr() {
        String text = "(2 + 3 + (3 + -4)) >= 1 && a > 2";
        ExpParser parser = new ExpParser(text);

        Expr expr = parser.parseExp(0);
        System.out.println(expr);
    }
}