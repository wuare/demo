package top.wuare.lang.env.builtin.cmm;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.builtin.BuiltInFunc;

import java.math.BigDecimal;
import java.util.List;

public class TimeBuiltInFunc implements BuiltInFunc {

    @Override
    public Object execute(List<Object> args, Console console) {
        return new BigDecimal(Long.toString(System.currentTimeMillis()));
    }

    @Override
    public int args() {
        return 0;
    }
}
