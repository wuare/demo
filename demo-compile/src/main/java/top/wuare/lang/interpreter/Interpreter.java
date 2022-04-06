package top.wuare.lang.interpreter;

import top.wuare.lang.ast.AST;
import top.wuare.lang.ast.expr.*;
import top.wuare.lang.ast.statement.*;
import top.wuare.lang.env.Console;
import top.wuare.lang.env.EnclosedScopeSymbolTable;
import top.wuare.lang.env.buildin.*;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.lexer.TokenType;
import top.wuare.lang.parser.Parser;
import top.wuare.lang.type.BreakVal;
import top.wuare.lang.type.ReturnVal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {

    private EnclosedScopeSymbolTable scopeSymbolTable = new EnclosedScopeSymbolTable();
    private static final Map<String, BuildInFunc> buildInFuncTable = new HashMap<>();
    private final Console console = new Console();

    static {
        buildInFuncTable.put("print", new PrintBuildInFunc());
        buildInFuncTable.put("time", new TimeBuildInFunc());
        buildInFuncTable.put("fileRead", new FileReadBuildInFunc());
        buildInFuncTable.put("fileWrite", new FileWriteBuildInFunc());
        buildInFuncTable.put("fileAppend", new FileAppendBuildInFunc());
    }

    private final Parser parser;

    public Interpreter(String text) {
        this.parser = new Parser(text);
    }

    public Object eval() {
        AST ast = parser.parse();
        try {
            return eval(ast);
        } catch (ReturnVal re) {
            return re.getVal();
        }
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
        if (ast instanceof ArrayExpr) {
            return evalArrayExpr((ArrayExpr) ast);
        }
        if (ast instanceof ArrayIndexExpr) {
            return evalArrayIndexExpr((ArrayIndexExpr) ast);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Object evalAssignExpr(AssignExpr ast) {
        Expr identExpr = ast.getIdentExpr();
        if (identExpr instanceof IdentExpr) {
            Token token = ((IdentExpr) identExpr).getToken();
            if (!scopeSymbolTable.containsKey(token.getText())) {
                throw new RuntimeException("变量[" + token.getText() + "]未定义");
            }
            scopeSymbolTable.assign(token.getText(), evalExpr(ast.getExpr()));
            return null;
        }
        // 数组赋值
        if (identExpr instanceof ArrayIndexExpr) {
            ArrayIndexExpr aExpr = (ArrayIndexExpr) identExpr;
            Expr left = aExpr.getExpr();
            if (!(left instanceof IdentExpr)) {
                throw new RuntimeException("数组赋值标识符错误，在第" + aExpr.getToken().getLine() + "行，第"
                        + aExpr.getToken().getColumn() + "列");
            }
            Token token = ((IdentExpr) left).getToken();
            if (!scopeSymbolTable.containsKey(token.getText())) {
                throw new RuntimeException("数组变量[" + token.getText() + "]未定义");
            }
            Object arr = scopeSymbolTable.get(token.getText());
            if (!(arr instanceof List)) {
                throw new RuntimeException("变量[" + token.getText() + "]不是数组类型");
            }
            Object indexVal = evalExpr(aExpr.getIndexExpr());
            List<Object> list = (List<Object>) arr;
            checkArray(indexVal, list, token);
            BigDecimal index = (BigDecimal) indexVal;
            list.set(index.intValue(), evalExpr(ast.getExpr()));
        }

        return null;
    }

    private Object evalCallExpr(CallExpr ast) {
        enterNewScopeSymbolTable();
        Token nameToken = ast.getName();
        BuildInFunc buildInFunc = buildInFuncTable.get(nameToken.getText());
        if (buildInFunc != null) {
            int argSize = buildInFunc.args();
            if (argSize != -1 && argSize != ast.getArgs().size()) {
                throw new RuntimeException("函数[" + nameToken.getText() + "]参数个数不匹配，应该传入" + argSize + "个参数");
            }
            List<Object> args = new ArrayList<>();
            for (Expr expr : ast.getArgs()) {
                args.add(eval(expr));
            }
            Object buildInRes = buildInFunc.execute(args, console);
            exitCurScopeSymbolTable();
            return buildInRes;
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
        Object reVal = null;
        try {
            evalBlock(stmt.getBlock());
        } catch (ReturnVal re) {
            reVal = re.getVal();
        }
        exitCurScopeSymbolTable();
        return reVal;
    }

    private Object evalIdentExpr(IdentExpr ast) {
        Token token = ast.getToken();
        switch (token.getType()) {
            case NUMBER:
                return new BigDecimal(token.getText());
            case STRING:
                return token.getText();
            case TRUE:
                return true;
            case FALSE:
                return false;
            case IDENT:
                if (!scopeSymbolTable.containsKey(token.getText())) {
                    throw new RuntimeException("变量[" + token.getText() + "]未定义");
                }
                return scopeSymbolTable.get(token.getText());
            case NIL:
            default:
                return null;
        }
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
                return ((BigDecimal) leftVal).divide((BigDecimal) rightVal, 12, RoundingMode.DOWN).stripTrailingZeros();
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
        if (left == null && right == null) {
            return false;
        }
        if (left instanceof Boolean && right instanceof Boolean) {
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
        if (left == null && right == null) {
            return true;
        }
        if (left instanceof Boolean && right instanceof Boolean) {
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
                if (exprVal instanceof BigDecimal) {
                    return BigDecimal.ZERO.subtract((BigDecimal) exprVal);
                }
                if (exprVal instanceof Boolean) {
                    return !((Boolean) exprVal);
                }
                throw new RuntimeException("计算一元表达式时错误，不是数字或布尔类型，在第" + token.getLine() + "行，第" + token.getColumn() + "列");
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

    private Object evalArrayExpr(ArrayExpr ast) {
        List<Object> list = new ArrayList<>();
        List<Expr> items = ast.getItems();
        for (Expr item : items) {
            list.add(evalExpr(item));
        }
        return list;
    }

    private Object evalArrayIndexExpr(ArrayIndexExpr ast) {
        Token token = ast.getToken();
        Object arr = evalExpr(ast.getExpr());
        if (!(arr instanceof List)) {
            throw new RuntimeException("数组标识错误，在第" + token.getLine() + "行，第" + token.getColumn() + "列附近");
        }
        Object io = evalExpr(ast.getIndexExpr());
        List<?> list = (List<?>) arr;
        checkArray(io, list, token);
        BigDecimal index = (BigDecimal) io;
        return list.get(index.intValue());
    }

    private void checkArray(Object indexVal, List<?> list, Token token) {
        if (!(indexVal instanceof BigDecimal)) {
            throw new RuntimeException("数组下标错误，在第" + token.getLine() + "行，第" + token.getColumn() + "列附近");
        }
        BigDecimal index = (BigDecimal) indexVal;
        if (new BigDecimal(index.intValue()).compareTo(index) != 0) {
            throw new RuntimeException("数组下标必须为整数，在第" + token.getLine() + "行，第" + token.getColumn() + "列附近");
        }
        if (index.intValue() < 0) {
            throw new RuntimeException("数组下标不能小于0，在第" + token.getLine() + "行，第" + token.getColumn() + "列附近");
        }
        if (index.intValue() > list.size() - 1) {
            throw new RuntimeException("数组下标超出范围，在第" + token.getLine() + "行，第" + token.getColumn() + "列附近");
        }
    }

    private Object evalBlock(Block ast) {
        if (ast == null) {
            return null;
        }
        List<Stmt> stmts = ast.getStmts();
        for (Stmt stmt : stmts) {
            evalStmt(stmt);
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
        if (ast instanceof ForStmt) {
            return evalForStmt((ForStmt) ast);
        }
        if (ast instanceof ReturnStmt) {
            return evalReturnStmt((ReturnStmt) ast);
        }
        if (ast instanceof ExprStmt) {
            return evalExprStmt((ExprStmt) ast);
        }
        if (ast instanceof BreakStmt) {
            return evalBreakStmt((BreakStmt) ast);
        }
        return null;
    }


    private Object evalReturnStmt(ReturnStmt ast) {
        Object exprVal = evalExpr(ast.getExpr());
        throw new ReturnVal(exprVal);
    }

    private Object evalDeclareStmt(VarDeclareStmt ast) {
        Token token = ast.getIdent();
        if (scopeSymbolTable.containsKey(token.getText())) {
            throw new RuntimeException("变量[" + token.getText() + "]已经定义");
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
        try {
            enterNewScopeSymbolTable();
            Object exprVal = evalExpr(ast.getExpr());
            if (exprVal instanceof Boolean && (boolean) exprVal) {
                evalBlock(ast.getThen());
            } else {
                evalBlock(ast.getEls());
            }
        } finally {
            exitCurScopeSymbolTable();
        }
        return null;
    }

    private Object evalWhileStmt(WhileStmt ast) {
        Object exprVal;
        while ((exprVal = evalExpr(ast.getExpr())) instanceof Boolean && (boolean) exprVal) {
            try {
                enterNewScopeSymbolTable();
                evalBlock(ast.getBlock());
            } catch (BreakVal ignored) {
                break;
            } finally {
                exitCurScopeSymbolTable();
            }
        }
        return null;
    }

    private Object evalForStmt(ForStmt ast) {
        enterNewScopeSymbolTable();
        try {
            Stmt initStmt = ast.getInitStmt();
            if (initStmt != null) {
                evalStmt(initStmt);
            }
            Expr expr = ast.getExpr();
            while (evalForExpr(expr)) {
                try {
                    enterNewScopeSymbolTable();
                    evalBlock(ast.getBlock());
                } finally {
                    exitCurScopeSymbolTable();
                }
                Expr updateExpr = ast.getUpdateExpr();
                if (updateExpr != null) {
                    evalExpr(updateExpr);
                }
            }

        } catch (BreakVal ignored) {
            // pass
        } finally {
            exitCurScopeSymbolTable();
        }
        return null;
    }

    private boolean evalForExpr(Expr expr) {
        if (expr == null) {
            return true;
        }
        Object res = evalExpr(expr);
        return res instanceof Boolean && ((Boolean) res);
    }

    private Object evalExprStmt(ExprStmt ast) {
        return evalExpr(ast.getExpr());
    }

    private Object evalBreakStmt(BreakStmt ast) {
        throw new BreakVal();
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
