package top.wuare.lang.interpreter;

import org.junit.Assert;
import org.junit.Test;
import top.wuare.lang.type.ReturnValue;

import java.math.BigDecimal;

public class InterpreterTest {

    @Test
    public void testEval() {
        Interpreter interpreter = new Interpreter("var a = 1; return a+1;");
        Object eval = interpreter.eval();
        Assert.assertNotNull(eval);
        Assert.assertTrue(eval instanceof ReturnValue);
        ReturnValue val = (ReturnValue) eval;
        Assert.assertNotNull(val.getVal());
        Assert.assertTrue(val.getVal() instanceof BigDecimal);
        BigDecimal num = (BigDecimal) val.getVal();
        Assert.assertEquals(num.intValue(), 2);
    }

    @Test
    public void testBlock() {
        String code = "var a = 1;            " +
                "      var b = 2;            " +
                "      func a(c) {           " +
                "          if (1+1 == 4) {   " +
                "              return a+b;   " +
                "          }                 " +
                "          return c;         " +
                "      }                     " +
                "      a(111);               ";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }
}
