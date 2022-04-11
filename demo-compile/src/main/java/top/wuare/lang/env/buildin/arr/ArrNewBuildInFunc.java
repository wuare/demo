package top.wuare.lang.env.buildin.arr;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.buildin.BuildInFunc;
import top.wuare.lang.type.Arr;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class ArrNewBuildInFunc implements BuildInFunc {

    @Override
    public Object execute(List<Object> args, Console console) {
        Object arg = args.get(0);
        if (!(arg instanceof BigDecimal)) {
            throw new RuntimeException("执行内置函数[arrNew]错误，参数只能为数字类型");
        }
        BigDecimal num = (BigDecimal) arg;
        int numVal = num.intValue();
        if (new BigDecimal(numVal).compareTo(num) != 0) {
            throw new RuntimeException("执行内置函数[arrNew]错误，参数必须为整数");
        }

        if (numVal < 0) {
            throw new RuntimeException("执行内置函数[arrNew]错误，参数不能小于0");
        }
        if (numVal > 65535) {
            throw new RuntimeException("执行内置函数[arrNew]错误，参数不能大于65535");
        }
        return new Arr<>(Arrays.asList(new Object[numVal]));
    }

    @Override
    public int args() {
        return 1;
    }
}
