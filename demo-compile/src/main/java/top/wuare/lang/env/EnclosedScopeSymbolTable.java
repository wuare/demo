package top.wuare.lang.env;

import java.util.HashMap;
import java.util.Map;

public class EnclosedScopeSymbolTable {

    private final Map<String, Object> table = new HashMap<>();
    private EnclosedScopeSymbolTable parent;

    public Map<String, Object> getTable() {
        return table;
    }

    public EnclosedScopeSymbolTable getParent() {
        return parent;
    }

    public void setParent(EnclosedScopeSymbolTable parent) {
        this.parent = parent;
    }

    public boolean containsKey(String text) {
        boolean b = table.containsKey(text);
        if (!b && parent != null) {
            return parent.containsKey(text);
        }
        return b;
    }

    public void put(String text, Object val) {
        table.put(text, val);
    }

    public Object get(String text) {
        if (table.containsKey(text)) {
            return table.get(text);
        }
        if (parent != null) {
            return parent.get(text);
        }
        return null;
    }
}
