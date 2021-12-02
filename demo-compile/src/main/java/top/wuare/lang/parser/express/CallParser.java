package top.wuare.lang.parser.express;

import top.wuare.lang.ast.expr.CallExpr;
import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.ast.expr.IdentExpr;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.lexer.TokenType;
import top.wuare.lang.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class CallParser implements InfixParser {

    private final int precedence;

    public CallParser(int precedence) {
        this.precedence = precedence;
    }

    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        if (!(left instanceof IdentExpr)) {
            throw new RuntimeException("syntax error when parse function call, expected function name, but get other");
        }
        IdentExpr identExpr = (IdentExpr) left;
        CallExpr callExpr = new CallExpr();
        callExpr.setName(identExpr.getToken());
        Token curToken = parser.getCurToken();
        if (curToken != null && curToken.getType() == TokenType.RPAREN) {
            parser.consume();
            return callExpr;
        }
        Expr expr = parser.parseExp(0);
        List<Expr> argsList = new ArrayList<>();
        argsList.add(expr);
        Token cur;
        if ((cur = parser.getCurToken()) != null && cur.getType() == TokenType.COMMA) {
            parser.consume();
            argsList.add(parser.parseExp(0));
        }
        parser.want(TokenType.RPAREN);
        parser.consume();
        callExpr.setArgs(argsList);
        return callExpr;
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }
}
