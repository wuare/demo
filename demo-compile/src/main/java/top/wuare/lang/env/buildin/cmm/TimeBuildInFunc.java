package top.wuare.lang.env.buildin.cmm;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.buildin.BuildInFunc;

import java.math.BigDecimal;
import java.util.List;

public class TimeBuildInFunc implements BuildInFunc {

    @Override
    public Object execute(List<Object> args, Console console) {
        return new BigDecimal(Long.toString(System.currentTimeMillis()));
    }

    @Override
    public int args() {
        return 0;
    }
}
