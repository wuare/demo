package top.wuare.lang.interpreter;

import org.junit.Assert;
import org.junit.Test;
import top.wuare.lang.env.Console;

import java.math.BigDecimal;

public class InterpreterTest {

    @Test
    public void testEval() {
        Interpreter interpreter = new Interpreter("var a = 1; return a+1;");
        Object eval = interpreter.eval();
        Assert.assertNotNull(eval);
        Assert.assertTrue(eval instanceof BigDecimal);
        BigDecimal num = (BigDecimal) eval;
        Assert.assertEquals(num.intValue(), 2);
    }

    @Test
    public void testBlock() {
        String code = "var a = 1;            " +
                "      var b = 2;            " +
                "      func a0(c) {           " +
                "          if (1+1 == 4) {   " +
                "              return a+b;   " +
                "          }                 " +
                "          return c;         " +
                "      }                     " +
                "      var b0 = a0(111);               ";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test
    public void testConsole() {
        String code = "var a = 1;            " +
                "      var b = 2;            " +
                "      print(a);             ";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        Console console = interpreter.getConsole();
        System.out.println(console.toString());
    }

    @Test
    public void testWhile() {
        String code = "while (1 + 1 != 2) { 2+2; }";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test
    public void testAssign() {
        String code = "var a; a = 1;";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test
    public void testString() {
        String code = "var a = \"1\"; print(\"123\");";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test
    public void testFunc() {
        String code = "func rec(num){                  " +
                "          if(num<=1) {                " +
                "              return 1;               " +
                "          } else {                    " +
                "              return num * rec(num-1);" +
                "          }                           " +
                "      }                               " +
                "      var a = rec(3);                 " +
                "      print(a);";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test
    public void testAddOperator() {
        String code = "var a = 1 + \"2\"; print(a);";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test(expected = RuntimeException.class)
    public void testAddOperator1() {
        String code = "var a; print(a + 1);";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test
    public void testEqualOperator() {
        String code = "print(\"a\" == \"a\");";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test
    public void testEqualOperator1() {
        String code = "print(\"a\" == 1);";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }

    @Test
    public void testReturnValue() {
        String code = "func a(num) {\n" +
                "\tif (1 == 1) {\n" +
                "\t\tif (num > 1) {\n" +
                "\t\t\treturn num;\n" +
                "\t\t} else {\n" +
                "\t\t\treturn 0;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n" +
                "print(a(2));";
        Interpreter interpreter = new Interpreter(code);
        Object eval = interpreter.eval();
        System.out.println(eval);
    }
}
