package top.wuare.lang.env.builtin.cmm;

import top.wuare.lang.ast.statement.FuncDeclareStmt;
import top.wuare.lang.env.Console;
import top.wuare.lang.env.builtin.BuiltInFunc;

import java.math.BigDecimal;
import java.util.List;

public class TypeBuiltInFunc implements BuiltInFunc {

    @Override
    public Object execute(List<Object> args, Console console) {
        Object arg = args.get(0);
        if (arg == null) {
            return "nil";
        }
        if (arg instanceof List<?>) {
            return "array";
        }
        if (arg instanceof String) {
            return "string";
        }
        if (arg instanceof BigDecimal) {
            return "number";
        }
        if (arg instanceof FuncDeclareStmt) {
            return "function";
        }
        if (arg instanceof Boolean) {
            return "boolean";
        }
        throw new RuntimeException("执行内置函数[type]错误，未知的参数类型");
    }

    @Override
    public int args() {
        return 1;
    }
}
