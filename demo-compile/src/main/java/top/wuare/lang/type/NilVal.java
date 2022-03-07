package top.wuare.lang.type;

public class NilVal {

    private NilVal() {
    }

    public static final NilVal NIL = new NilVal();

    @Override
    public String toString() {
        return "nil";
    }
}
