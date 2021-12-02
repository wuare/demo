package top.wuare.lang.ast.expr;

public class IdentExpr implements Expr {

    private final String text;

    public IdentExpr(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
