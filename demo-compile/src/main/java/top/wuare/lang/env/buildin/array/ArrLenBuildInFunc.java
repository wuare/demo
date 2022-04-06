package top.wuare.lang.env.buildin.array;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.buildin.BuildInFunc;

import java.math.BigDecimal;
import java.util.List;

public class ArrLenBuildInFunc implements BuildInFunc {

    @Override
    public Object execute(List<Object> args, Console console) {
        Object arr = args.get(0);
        if (!(arr instanceof List<?>)) {
            throw new RuntimeException("执行内置函数[arrLen]错误，参数不是数组类型");
        }
        return new BigDecimal(((List<?>) arr).size());
    }

    @Override
    public int args() {
        return 1;
    }
}
