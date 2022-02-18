package top.wuare.lang.type;

public class ReturnVal extends RuntimeException {

    private final Object val;

    public ReturnVal(Object val) {
        this.val = val;
    }

    public Object getVal() {
        return val;
    }
}
