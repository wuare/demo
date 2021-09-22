package top.wuare.json.data;

import java.util.Arrays;

public class ArrayData {

    private int[] ids;

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "ArrayData{" +
                "ids=" + Arrays.toString(ids) +
                '}';
    }
}
