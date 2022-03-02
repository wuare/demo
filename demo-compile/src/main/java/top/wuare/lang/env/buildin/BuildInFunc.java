package top.wuare.lang.env.buildin;

import top.wuare.lang.env.Console;

import java.util.List;

public interface BuildInFunc {
    Object execute(List<Object> args, Console console);
    int args();
}
