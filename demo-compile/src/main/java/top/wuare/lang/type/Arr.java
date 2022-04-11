package top.wuare.lang.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Arr<E> extends ArrayList<E> {

    public Arr(int initialCapacity) {
        super(initialCapacity);
    }

    public Arr() {
    }

    public Arr(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : (e == null ? "nil" : e));
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}
