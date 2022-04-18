package top.wuare.lang.env.builtin.cmm;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.builtin.BuiltInFunc;

import java.math.BigDecimal;
import java.util.List;

public class PrintBuiltInFunc implements BuiltInFunc {

    @Override
    public Object execute(List<Object> args, Console console) {
        for (Object arg : args) {
            if (arg == null) {
                console.write("nil");
                continue;
            }

            if (arg instanceof BigDecimal) {
                console.write(((BigDecimal) arg).toPlainString());
                continue;
            }
            console.write(arg.toString());
        }
        return null;
    }

    @Override
    public int args() {
        return -1;
    }
}
