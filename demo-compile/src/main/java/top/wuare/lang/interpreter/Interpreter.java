package top.wuare.lang.interpreter;

import top.wuare.lang.ast.AST;
import top.wuare.lang.ast.expr.*;
import top.wuare.lang.ast.statement.*;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.lexer.TokenType;
import top.wuare.lang.parser.Parser;
import top.wuare.lang.type.ReturnValue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    private Map<String, Object> symbolTable = new HashMap<>();
    private final Map<String, FuncDeclareStmt> funcTable = new HashMap<>();

    private final Parser parser;

    public Interpreter(String text) {
        this.parser = new Parser(text);
    }

    public Object eval() {
        AST ast = parser.parse();
        return eval(ast);
    }

    private Object eval(AST ast) {
        if (ast instanceof Expr) {
            return evalExpr((Expr) ast);
        }
        if (ast instanceof Block) {
            return evalBlock((Block) ast);
        }
        if (ast instanceof Stmt) {
            return evalStmt((Stmt) ast);
        }
        throw new RuntimeException("Unknown AST");
    }

    private Object evalExpr(Expr ast) {
        if (ast instanceof AssignExpr) {
            return evalAssignExpr((AssignExpr) ast);
        }
        if (ast instanceof CallExpr) {
            return evalCallExpr((CallExpr) ast);
        }
        if (ast instanceof IdentExpr) {
            return evalIdentExpr((IdentExpr) ast);
        }
        if (ast instanceof OperatorExpr) {
            return evalOperatorExpr((OperatorExpr) ast);
        }
        if (ast instanceof PrefixExpr) {
            return evalPrefixExpr((PrefixExpr) ast);
        }
        return null;
    }

    private Object evalAssignExpr(AssignExpr ast) {
        Token token = ast.getToken();
        if (!symbolTable.containsKey(token.getText())) {
            throw new RuntimeException("变量[" + token.getText() + "]未定义");
        }
        symbolTable.put(token.getText(), evalExpr(ast.getExpr()));
        return null;
    }
    private Object evalCallExpr(CallExpr ast) {
        Token nameToken = ast.getName();
        FuncDeclareStmt stmt = funcTable.get(nameToken.getText());
        if (stmt == null) {
            throw new RuntimeException("函数[" + nameToken.getText() + "]未定义");
        }
        if (ast.getArgs().size() != stmt.getArgs().size()) {
            throw new RuntimeException("函数[" + nameToken.getText() + "]参数数量不一致");
        }
        for (int i = 0; i < stmt.getArgs().size(); i++) {
            Expr stmtExpr = stmt.getArgs().get(i);
            if (!(stmtExpr instanceof IdentExpr)) {
                throw new RuntimeException("函数[" + nameToken.getText() + "]定义参数错误，应使用标识符");
            }
            IdentExpr arg = (IdentExpr) stmtExpr;
            Object exprVal = evalExpr(ast.getArgs().get(i));
            symbolTable.put(arg.getToken().getText(), exprVal);
        }
        return eval(stmt.getBlock());
    }

    private Object evalIdentExpr(IdentExpr ast) {
        Token token = ast.getToken();
        if (token.getType() == TokenType.NUMBER) {
            return new BigDecimal(token.getText());
        }
        if (token.getType() == TokenType.STRING) {
            return token.getText();
        }
        if (token.getType() == TokenType.IDENT) {
            if (!symbolTable.containsKey(token.getText())) {
                throw new RuntimeException("变量[" + token.getText() + "]未定义");
            }
            return symbolTable.get(token.getText());
        }
        return null;
    }

    private Object evalOperatorExpr(OperatorExpr ast) {
        Token token = ast.getToken();
        Object leftVal = evalExpr(ast.getLeft());
        Object rightVal = evalExpr(ast.getRight());
        switch (token.getType()) {
            case ADD:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).add((BigDecimal) rightVal);
            case SUB:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).subtract((BigDecimal) rightVal);
            case MUL:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).multiply((BigDecimal) rightVal);
            case DIV:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).divide((BigDecimal) rightVal, RoundingMode.DOWN);
            case GT:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) > 0;
            case GE:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) >= 0;
            case LT:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) < 0;
            case LE:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) <= 0;
            case EQUAL:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) == 0;
            case NOTEQUAL:
                findValAndCheckNumber(leftVal, token);
                findValAndCheckNumber(rightVal, token);
                return ((BigDecimal) leftVal).compareTo((BigDecimal) rightVal) != 0;
            case AND:
                findValAndCheckBoolean(leftVal, token);
                findValAndCheckBoolean(rightVal, token);
                return ((Boolean) leftVal) && ((Boolean) rightVal);
            case OR:
                findValAndCheckBoolean(leftVal, token);
                findValAndCheckBoolean(rightVal, token);
                return ((Boolean) leftVal) || ((Boolean) rightVal);

        }
        return null;
    }

    private Object evalPrefixExpr(PrefixExpr ast) {
        Object exprVal = evalExpr(ast.getOperand());
        Token token = ast.getToken();
        switch (token.getType()) {
            case SUB:
                if (!(exprVal instanceof BigDecimal)) {
                    throw new RuntimeException("计算一元表达式时错误，不是数字，在第" + token.getLine() + "行，第" + token.getColumn() + "列");
                }
                return BigDecimal.ZERO.subtract((BigDecimal) exprVal);
            case IDENT:
                if (!symbolTable.containsKey(token.getText())) {
                    throw new RuntimeException("变量[" + token.getText() + "]未定义");
                }
                return symbolTable.get(token.getText());
            case NUMBER:
                return new BigDecimal(token.getText());
            case BANG:
                if (!(exprVal instanceof Boolean)) {
                    throw new RuntimeException("计算一元表达式时错误，不是布尔表达式，在第" + token.getLine() + "行，第" + token.getColumn() + "列");
                }
                return !((Boolean) exprVal);
            case LPAREN:
                return exprVal;
        }
        return null;
    }

    private Object evalBlock(Block ast) {
        if (ast == null) {
            return null;
        }
        List<Stmt> stmts = ast.getStmts();
        for (Stmt stmt : stmts) {
            Object val = evalStmt(stmt);
            if (val instanceof ReturnValue) {
                return val;
            }
        }
        return null;
    }

    private Object evalStmt(Stmt ast) {
        if (ast instanceof VarDeclareStmt) {
            return evalDeclareStmt((VarDeclareStmt) ast);
        }
        if (ast instanceof FuncDeclareStmt) {
            return evalFuncDeclareStmt((FuncDeclareStmt) ast);
        }
        if (ast instanceof IfStmt) {
            return evalIfStmt((IfStmt) ast);
        }
        if (ast instanceof WhileStmt) {
            return evalWhileStmt((WhileStmt) ast);
        }
        if (ast instanceof ReturnStmt) {
            return evalReturnStmt((ReturnStmt) ast);
        }
        if (ast instanceof ExprStmt) {
            return evalExprStmt((ExprStmt) ast);
        }
        return null;
    }



    private Object evalReturnStmt(ReturnStmt ast) {
        Object exprVal = evalExpr(ast.getExpr());
        return new ReturnValue(exprVal);
    }

    private Object evalDeclareStmt(VarDeclareStmt ast) {
        Token token = ast.getIdent();
        if (symbolTable.containsKey(token.getText())) {
            throw new RuntimeException("变量[" + token.getText() + "]已经定义，暂不支持作用域");
        }
        Object exprVal = evalExpr(ast.getExpr());
        symbolTable.put(token.getText(), exprVal);
        return null;
    }

    private Object evalFuncDeclareStmt(FuncDeclareStmt ast) {
        Token token = ast.getName();
        if (funcTable.containsKey(token.getText())) {
            throw new RuntimeException("函数[" + token.getText() + "]已经定义");
        }
        funcTable.put(token.getText(), ast);
        return null;
    }

    private Object evalIfStmt(IfStmt ast) {
        Object exprVal = evalExpr(ast.getExpr());
        if (exprVal instanceof Boolean && (boolean) exprVal) {
            return evalBlock(ast.getThen());
        } else {
            return evalBlock(ast.getEls());
        }
    }

    private Object evalWhileStmt(WhileStmt ast) {
        Object exprVal;
        while ((exprVal = evalExpr(ast.getExpr())) instanceof Boolean && (boolean) exprVal) {
            Object blockVal = evalBlock(ast.getBlock());
            if (blockVal instanceof ReturnValue) {
                return blockVal;
            }
        }
        return null;
    }

    private Object evalExprStmt(ExprStmt ast) {
        return evalExpr(ast.getExpr());
    }

    public void checkNumberType(Object val, Token token) {
        if (!(val instanceof BigDecimal)) {
            throw new RuntimeException("不是数字类型，在第" + token.getLine() + "行，第" + token.getColumn() + "列");
        }
    }

    public void checkBooleanType(Object val, Token token) {
        if (!(val instanceof Boolean)) {
            throw new RuntimeException("不是布尔类型，在第" + token.getLine() + "行，第" + token.getColumn() + "列");
        }
    }

    private void findValAndCheckNumber(Object obj, Token token) {
        Object val = obj;
        if (token.getType() == TokenType.IDENT) {
            if (!symbolTable.containsKey(token.getText())) {
                throw new RuntimeException("变量[" + token.getText() + "]未定义");
            }
            val = symbolTable.get(token.getText());
        }
        checkNumberType(val, token);
    }

    private void findValAndCheckBoolean(Object obj, Token token) {
        Object val = obj;
        if (token.getType() == TokenType.IDENT) {
            if (!symbolTable.containsKey(token.getText())) {
                throw new RuntimeException("变量[" + token.getText() + "]未定义");
            }
            val = symbolTable.get(token.getText());
        }
        checkBooleanType(val, token);
    }
}
