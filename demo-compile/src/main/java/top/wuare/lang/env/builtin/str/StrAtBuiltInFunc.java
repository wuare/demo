package top.wuare.lang.env.builtin.str;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.builtin.BuiltInFunc;

import java.math.BigDecimal;
import java.util.List;

public class StrAtBuiltInFunc implements BuiltInFunc {

    @Override
    public Object execute(List<Object> args, Console console) {
        Object str = args.get(0);
        Object idx = args.get(1);
        if (!(str instanceof String)) {
            throw new RuntimeException("执行内置函数[strAt]错误，第一个参数必须为字符串");
        }
        if (!(idx instanceof BigDecimal)) {
            throw new RuntimeException("执行内置函数[strAt]错误，第二个参数必须为数字");
        }
        BigDecimal idxDecimal = (BigDecimal) idx;
        int idxVal = idxDecimal.intValue();
        if (new BigDecimal(idxVal).compareTo(idxDecimal) != 0) {
            throw new RuntimeException("执行内置函数[strAt]错误，第二个参数必须为整数");
        }
        if (idxVal < 0) {
            throw new RuntimeException("执行内置函数[strAt]错误，第二个参数不能小于0");
        }
        String strVal = (String) str;
        int len = strVal.length();
        if (idxVal > len - 1) {
            throw new RuntimeException("执行内置函数[strAt]错误，第二个参数超出了字符串长度");
        }
        return String.valueOf(strVal.charAt(idxVal));
    }

    @Override
    public int args() {
        return 2;
    }
}
