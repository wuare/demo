package top.wuareb.highlight.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Sets {

    private Sets() {
    }

    @SafeVarargs
    public static <E> Set<E> of(E... e) {
        return new HashSet<>(Arrays.asList(e));
    }
}
