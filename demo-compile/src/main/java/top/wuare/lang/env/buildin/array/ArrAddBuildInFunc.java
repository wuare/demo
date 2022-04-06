package top.wuare.lang.env.buildin.array;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.buildin.BuildInFunc;

import java.util.List;

public class ArrAddBuildInFunc implements BuildInFunc {

    @SuppressWarnings("unchecked")
    @Override
    public Object execute(List<Object> args, Console console) {
        Object arr = args.get(0);
        Object val = args.get(1);
        if (!(arr instanceof List<?>)) {
            throw new RuntimeException("执行内置函数[arrAdd]错误，参数不是数组类型");
        }
        ((List<Object>) arr).add(val);
        return null;
    }

    @Override
    public int args() {
        return 2;
    }
}
