package top.wuare.lang.env.buildin;

import top.wuare.lang.env.Console;
import top.wuare.lang.type.ReturnValue;

import java.math.BigDecimal;

public class PrintBuildInFunc implements BuildInFunc {

    @Override
    public void execute(Object arg, Console console) {
        if (arg == null) {
            console.write("null");
            return;
        }
        if (arg instanceof ReturnValue) {
            arg = ((ReturnValue) arg).getVal();
        }

        if (arg instanceof BigDecimal) {
            console.write(((BigDecimal) arg).toPlainString());
            return;
        }
        console.write(arg.toString());
    }
}
