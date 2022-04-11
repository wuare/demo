package top.wuare.lang.env.buildin.cmm;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.buildin.BuildInFunc;

import java.math.BigDecimal;
import java.util.List;

public class LenBuildInFunc implements BuildInFunc {

    @Override
    public Object execute(List<Object> args, Console console) {
        Object arg = args.get(0);
        if (arg instanceof List<?>) {
            return new BigDecimal(((List<?>) arg).size());
        }
        if (arg instanceof String) {
            return new BigDecimal(((String) arg).length());
        }

        throw new RuntimeException("执行内置函数[len]错误，参数只能为数组或字符串类型");

    }

    @Override
    public int args() {
        return 1;
    }
}
