package top.wuare.express.ast;

public class NameExpr implements Expr {

    private final String text;

    public NameExpr(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
