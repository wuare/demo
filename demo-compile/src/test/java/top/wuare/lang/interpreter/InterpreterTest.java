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
    public void testFor() {
        String code = "for (var i = 1; i < 10; i = i + 1) {print(i);}";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        System.out.println(interpreter.getConsole().toString());
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

    @Test
    public void testTimeFunc() {
        String code = "var start = time(); var aa = 0; while (aa < 100) {print(aa);print(\"\n\"); aa = aa + 1;}var end = time(); print(end - start);";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        System.out.println(interpreter.getConsole().toString());
    }

    @Test
    public void testBreak() {
        String code = "var i = 0;\n" +
                "func a() {\n" +
                "    while(true) {\n" +
                "        i = i + 1;\n" +
                "        if (i > 2) {\n" +
                "            break;\n" +
                "        }\n" +
                "    }\n" +
                "    print(i);\n" +
                "}\n" +
                "a();";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        System.out.println(interpreter.getConsole().toString());
    }

    @Test
    public void callMultipleArgs() {
        String code = "print(1,2,3);";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        System.out.println(interpreter.getConsole().toString());
    }

    @Test
    public void someTest() {
        String code = "\n" +
                "func 乘算口訣() {\n" +
                "  var 甲 = 1;\n" +
                "  while (true) {\n" +
                "    if (甲 >= 10) {\n" +
                "      break;\n" +
                "    }\n" +
                "    var 乙 = 1;\n" +
                "    var _ans1 = 甲 + 乙;\n" +
                "    var 丙 = _ans1;\n" +
                "    while (true) {\n" +
                "      if (乙 >= 丙) {\n" +
                "        break;\n" +
                "      }\n" +
                "      var _ans2 = 甲 * 乙;\n" +
                "      var 果 = _ans2;\n" +
                "      if (果 >= 10) {\n" +
                "        var _ans3 = 乙;\n" +
                "        var _ans4 = 甲;\n" +
                "        var _ans5 = 果;\n" +
                "        print(_ans3,_ans4,_ans5);\n" +
                "      } else {\n" +
                "        var _ans6 = 乙;\n" +
                "        var _ans7 = 甲;\n" +
                "        var _ans8 = \"得\";\n" +
                "        var _ans9 = 果;\n" +
                "        print(_ans6, _ans7, _ans8, _ans9);\n" +
                "      }\n" +
                "      var _ans10 = 乙 + 1;\n" +
                "      乙 = _ans10;\n" +
                "    }\n" +
                "    var _ans11 = 甲 + 1;\n" +
                "    甲 = _ans11;\n" +
                "  }\n" +
                "}\n" +
                "乘算口訣();";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        System.out.println(interpreter.getConsole().toString());
    }

    @Test
    public void testArrayIndex() {
        String code = "print([1,2][0]);";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        System.out.println(interpreter.getConsole().toString());
    }

    @Test
    public void tstArrayIndex0() {
        String code = "var a = [1,2]; print(a[0]);";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        System.out.println(interpreter.getConsole().toString());
    }

    @Test
    public void testArrayAssign() {
        String code = "var a = [1,2,3]; a[0] = 2; print(a);";
        Interpreter interpreter = new Interpreter(code);
        interpreter.eval();
        System.out.println(interpreter.getConsole().toString());
    }
}
