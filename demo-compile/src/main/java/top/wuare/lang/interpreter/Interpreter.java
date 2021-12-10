package top.wuare.lang.interpreter;

import top.wuare.lang.ast.AST;
import top.wuare.lang.ast.expr.*;
import top.wuare.lang.ast.statement.*;
import top.wuare.lang.env.Console;
import top.wuare.lang.env.EnclosedScopeSymbolTable;
import top.wuare.lang.env.buildin.BuildInFunc;
import top.wuare.lang.env.buildin.PrintBuildInFunc;
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

    private EnclosedScopeSymbolTable scopeSymbolTable = new EnclosedScopeSymbolTable();
    private static final Map<String, BuildInFunc> buildInFuncTable = new HashMap<>();
    private final Console console = new Console();

    static {
        buildInFuncTable.put("print", new PrintBuildInFunc());
    }

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
        if (!scopeSymbolTable.containsKey(token.getText())) {
            throw new RuntimeException("变量[" + token.getText() + "]未定义");
        }
        scopeSymbolTable.put(token.getText(), evalExpr(ast.getExpr()));
        return null;
    }
    private Object evalCallExpr(CallExpr ast) {
        enterNewScopeSymbolTable();
        Token nameToken = ast.getName();
        BuildInFunc buildInFunc = buildInFuncTable.get(nameToken.getText());
        if (buildInFunc != null) {
            for (Expr expr : ast.getArgs()) {
                buildInFunc.execute(eval(expr), console);
            }
            exitCurScopeSymbolTable();
            return null;
        }

        Object funcDeclare = scopeSymbolTable.get(nameToken.getText());
        if (!(funcDeclare instanceof FuncDeclareStmt)) {
            throw new RuntimeException("函数[" + nameToken.getText() + "]未定义");
        }
        FuncDeclareStmt stmt = (FuncDeclareStmt) funcDeclare;
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
            scopeSymbolTable.put(arg.getToken().getText(), exprVal);
        }
        Object val = evalFuncBlock(stmt.getBlock());
        exitCurScopeSymbolTable();
        return val instanceof ReturnValue ? ((ReturnValue) val).getVal() : val;
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
            if (!scopeSymbolTable.containsKey(token.getText())) {
                throw new RuntimeException("变量[" + token.getText() + "]未定义");
            }
            return scopeSymbolTable.get(token.getText());
        }
        return null;
    }

    private Object evalOperatorExpr(OperatorExpr ast) {
        Token token = ast.getToken();
        Object leftVal = evalExpr(ast.getLeft());
        Object rightVal = evalExpr(ast.getRight());
        switch (token.getType()) {
            case ADD:
                return evalAddOperatorExpr(token, leftVal, rightVal);
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
                return evalEqualOperatorExpr(token, leftVal, rightVal);
            case NOTEQUAL:
                return evalNotEqualOperatorExpr(token, leftVal, rightVal);
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

    private Object evalNotEqualOperatorExpr(Token token, Object leftVal, Object rightVal) {
        Object left = findVal(leftVal, token);
        Object right = findVal(rightVal, token);
        if (left instanceof BigDecimal && right instanceof BigDecimal) {
            return ((BigDecimal) left).compareTo((BigDecimal) right) != 0;
        }
        if (left instanceof String && right instanceof String) {
            return !left.equals(right);
        }
        return true;
    }

    private Object evalEqualOperatorExpr(Token token, Object leftVal, Object rightVal) {
        Object left = findVal(leftVal, token);
        Object right = findVal(rightVal, token);
        if (left instanceof BigDecimal && right instanceof BigDecimal) {
            return ((BigDecimal) left).compareTo((BigDecimal) right) == 0;
        }
        if (left instanceof String && right instanceof String) {
            return left.equals(right);
        }
        return false;
    }

    private Object evalAddOperatorExpr(Token token, Object leftVal, Object rightVal) {
        Object left = findVal(leftVal, token);
        Object right = findVal(rightVal, token);
        if (left instanceof BigDecimal && right instanceof BigDecimal) {
            return ((BigDecimal) left).add((BigDecimal) right);
        }
        if (left == null) {
            throw new RuntimeException("表达式的值为空，在第" + token.getLine() + "行，第" + token.getColumn() + "列+号左侧");
        }
        if (right == null) {
            throw new RuntimeException("表达式的值为空，在第" + token.getLine() + "行，第" + token.getColumn() + "列+号右侧附近");
        }
        return left.toString() + right;
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
                if (!scopeSymbolTable.containsKey(token.getText())) {
                    throw new RuntimeException("变量[" + token.getText() + "]未定义");
                }
                return scopeSymbolTable.get(token.getText());
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
            if (stmt instanceof ReturnStmt && val instanceof ReturnValue) {
                return val;
            }
        }
        return null;
    }

    // 为了解决返回值问题，如果函数中别的块（如IF块）返回，那么整个函数返回
    private Object evalFuncBlock(Block ast) {
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
        if (scopeSymbolTable.containsKey(token.getText())) {
            throw new RuntimeException("变量[" + token.getText() + "]已经定义，暂不支持作用域");
        }
        Object exprVal = evalExpr(ast.getExpr());
        scopeSymbolTable.put(token.getText(), exprVal);
        return null;
    }

    private Object evalFuncDeclareStmt(FuncDeclareStmt ast) {
        Token token = ast.getName();
        if (buildInFuncTable.containsKey(token.getText())) {
            throw new RuntimeException("函数名称[" + token.getText() + "]是内置函数，不能进行定义");
        }
        if (scopeSymbolTable.containsKey(token.getText())) {
            throw new RuntimeException("函数名称[" + token.getText() + "]已经被定义");
        }
        scopeSymbolTable.put(token.getText(), ast);
        return null;
    }

    private Object evalIfStmt(IfStmt ast) {
        enterNewScopeSymbolTable();
        Object exprVal = evalExpr(ast.getExpr());
        Object val;
        if (exprVal instanceof Boolean && (boolean) exprVal) {
            val = evalBlock(ast.getThen());
        } else {
            val = evalBlock(ast.getEls());
        }
        exitCurScopeSymbolTable();
        return val;
    }

    private Object evalWhileStmt(WhileStmt ast) {
        enterNewScopeSymbolTable();
        Object exprVal;
        while ((exprVal = evalExpr(ast.getExpr())) instanceof Boolean && (boolean) exprVal) {
            Object blockVal = evalBlock(ast.getBlock());
            if (blockVal instanceof ReturnValue) {
                exitCurScopeSymbolTable();
                return blockVal;
            }
        }
        exitCurScopeSymbolTable();
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
            if (!scopeSymbolTable.containsKey(token.getText())) {
                throw new RuntimeException("变量[" + token.getText() + "]未定义");
            }
            val = scopeSymbolTable.get(token.getText());
        }
        checkNumberType(val, token);
    }

    private Object findVal(Object obj, Token token) {
        Object val = obj;
        if (token.getType() == TokenType.IDENT) {
            if (!scopeSymbolTable.containsKey(token.getText())) {
                throw new RuntimeException("变量[" + token.getText() + "]未定义");
            }
            val = scopeSymbolTable.get(token.getText());
        }
        return val;
    }

    private void findValAndCheckBoolean(Object obj, Token token) {
        Object val = obj;
        if (token.getType() == TokenType.IDENT) {
            if (!scopeSymbolTable.containsKey(token.getText())) {
                throw new RuntimeException("变量[" + token.getText() + "]未定义");
            }
            val = scopeSymbolTable.get(token.getText());
        }
        checkBooleanType(val, token);
    }

    public void enterNewScopeSymbolTable() {
        EnclosedScopeSymbolTable parent = scopeSymbolTable;
        scopeSymbolTable = new EnclosedScopeSymbolTable();
        scopeSymbolTable.setParent(parent);
    }

    public void exitCurScopeSymbolTable() {
        scopeSymbolTable = scopeSymbolTable.getParent();
    }

    public Console getConsole() {
        return console;
    }
}
