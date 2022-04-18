package top.wuare.lang.env.builtin;

import top.wuare.lang.env.Console;

import java.util.List;

public interface BuiltInFunc {
    Object execute(List<Object> args, Console console);
    int args();
}
