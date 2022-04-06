package top.wuare.lang.ast.expr;

import java.util.ArrayList;
import java.util.List;

public class ArrayExpr implements Expr {

    private final List<Expr> items = new ArrayList<>();

    public List<Expr> getItems() {
        return items;
    }
}
