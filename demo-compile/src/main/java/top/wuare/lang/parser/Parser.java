package top.wuare.lang.parser;

import top.wuare.lang.ast.AST;
import top.wuare.lang.ast.expr.Expr;
import top.wuare.lang.ast.statement.*;
import top.wuare.lang.lexer.Lexer;
import top.wuare.lang.lexer.Token;
import top.wuare.lang.lexer.TokenType;
import top.wuare.lang.parser.express.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private final Lexer lexer;

    private Token curToken;
    private static final Map<TokenType, PrefixParser> mPrefixParseLets = new HashMap<>();
    private static final Map<TokenType, InfixParser> mInfixParseLets = new HashMap<>();

    public Parser(String text) {
        this.lexer = new Lexer(text);
        consume();
    }

    public Parser(InputStream in) {
        this.lexer = new Lexer(in);
        consume();
    }

    public AST parse() {
        return parseBlock();
    }

    // Block: Stmt...
    private Block parseBlock() {
        List<Stmt> stmts = new ArrayList<>();
        Stmt stmt;
        while ((stmt = parseStmt()) != null) {
            stmts.add(stmt);
        }
        return new Block(stmts);
    }

    // Stmt: DeclareStmt | ExprStmt | IfStmt | WhileStmt | ReturnStmt
    private Stmt parseStmt() {
        if (curToken == null) {
            return null;
        }
        TokenType type = curToken.getType();
        switch (type) {
            case VAR:
            case FUNC:
                return parseDeclareStmt();
            case IF:
                return parseIfStmt();
            case WHILE:
                return parseWhileStmt();
            case RETURN:
                return parseReturnStmt();
            case BREAK:
                return parseBreakStmt();
            case IDENT:
            case STRING:
            case NUMBER:
            case SUB:
            case BANG:
            case LPAREN:
                return parseExprStmt();
            default:
                return null;
        }
    }

    // VarDeclareStmt: VAR ident (= Expr)? ';'
    // FuncDeclareStmt: FUNC ident '(' (expr (',' expr)*)* ')' '{' Block '}'
    private Stmt parseDeclareStmt() {
        if (curToken.getType() == TokenType.VAR) {
            VarDeclareStmt stmt = new VarDeclareStmt();
            eat(TokenType.VAR);
            want(TokenType.IDENT);
            stmt.setIdent(curToken);
            consume();
            if (curToken != null && curToken.getType() == TokenType.ASSIGN) {
                consume();
                Expr expr = parseExp(0);
                stmt.setExpr(expr);
            }
            eat(TokenType.SEMICOLON);
            return stmt;
        }
        if (curToken.getType() == TokenType.FUNC) {
            FuncDeclareStmt stmt = new FuncDeclareStmt();
            eat(TokenType.FUNC);
            want(TokenType.IDENT);
            stmt.setName(curToken);
            consume();
            eat(TokenType.LPAREN);
            if (curToken != null && curToken.getType() != TokenType.RPAREN) {
                List<Expr> args = new ArrayList<>();
                Expr expr = parseExp(0);
                args.add(expr);
                while (curToken != null && curToken.getType() == TokenType.COMMA) {
                    consume();
                    args.add(parseExp(0));
                }
                stmt.setArgs(args);
            }
            eat(TokenType.RPAREN);
            eat(TokenType.LBRACE);
            if (curToken != null && curToken.getType() != TokenType.RBRACE) {
                stmt.setBlock(parseBlock());
            }
            eat(TokenType.RBRACE);
            return stmt;
        }
        throw new RuntimeException("syntax error, not support declare statement");
    }

    // IfStmt: IF '(' Expr ')' '{' Block '}' (ELSE '{' Block '}')?
    private Stmt parseIfStmt() {
        IfStmt stmt = new IfStmt();
        eat(TokenType.IF);
        eat(TokenType.LPAREN);
        Expr expr = parseExp(0);
        stmt.setExpr(expr);
        eat(TokenType.RPAREN);
        eat(TokenType.LBRACE);
        if (curToken != null && curToken.getType() != TokenType.RBRACE) {
            stmt.setThen(parseBlock());
        }
        eat(TokenType.RBRACE);
        if (curToken != null && curToken.getType() == TokenType.ELSE) {
            consume();
            eat(TokenType.LBRACE);
            if (curToken != null && curToken.getType() != TokenType.RBRACE) {
                stmt.setEls(parseBlock());
            }
            eat(TokenType.RBRACE);
        }
        return stmt;
    }

    // WhileStmt: WHILE '(' Expr ')' '{' Block '}'
    private Stmt parseWhileStmt() {
        WhileStmt stmt = new WhileStmt();
        eat(TokenType.WHILE);
        eat(TokenType.LPAREN);
        stmt.setExpr(parseExp(0));
        eat(TokenType.RPAREN);
        eat(TokenType.LBRACE);
        if (curToken != null && curToken.getType() == TokenType.RBRACE) {
            consume();
            return stmt;
        }
        stmt.setBlock(parseBlock());
        eat(TokenType.RBRACE);
        return stmt;
    }

    // ReturnStmt: RETURN (Expr)? ';'
    private Stmt parseReturnStmt() {
        ReturnStmt stmt = new ReturnStmt();
        eat(TokenType.RETURN);
        if (curToken != null && curToken.getType() == TokenType.SEMICOLON) {
            consume();
            return stmt;
        }
        stmt.setExpr(parseExp(0));
        eat(TokenType.SEMICOLON);
        return stmt;
    }

    // BreakStmt: BREAK ';'
    private Stmt parseBreakStmt() {
        Token t = curToken;
        eat(TokenType.BREAK);
        eat(TokenType.SEMICOLON);
        return new BreakStmt(t);
    }

    // ExprStmt: expr ';'
    private Stmt parseExprStmt() {
        Expr expr = parseExp(0);
        eat(TokenType.SEMICOLON);
        return new ExprStmt(expr);
    }


    public Expr parseExp(int precedence) {
        if (curToken == null) {
            return null;
        }
        PrefixParser prefixParser = mPrefixParseLets.get(curToken.getType());
        if (prefixParser == null) {
            return null;
        }
        Expr left = prefixParser.parse(this, curToken);
        while (precedence < getPrecedence()) {
            Token token = curToken;
            InfixParser infixParser = mInfixParseLets.get(curToken.getType());
            if (infixParser == null) {
                return left;
            }
            consume();
            left = infixParser.parse(this, left, token);
        }
        return left;
    }

    private int getPrecedence() {
        if (curToken == null) {
            return 0;
        }
        InfixParser infixParser = mInfixParseLets.get(curToken.getType());
        if (infixParser != null) {
            return infixParser.getPrecedence();
        }
        return 0;
    }

    public Token getCurToken() {
        return curToken;
    }

    public void consume() {
        curToken = lexer.nextToken();
    }

    public void want(TokenType type) {
        if (curToken != null && type == curToken.getType()) {
            return;
        }
        throw new RuntimeException("expect token type: " + type.getText() + ", but get type: "
                + (curToken == null ? "null" :
                curToken.getType().getText() + ", at line: " + curToken.getLine() + ", column: " + curToken.getColumn()));
    }

    private void eat(TokenType type) {
        if (curToken != null && type == curToken.getType()) {
            consume();
            return;
        }
        throw new RuntimeException("expect token type: " + type.getText() + ", but get type: "
                + (curToken == null ? "null" :
                curToken.getType().getText() + ", at line: " + curToken.getLine() + ", column: " + curToken.getColumn()));
    }

    static {
        register(TokenType.NUMBER, new IdentParser());
        register(TokenType.STRING, new IdentParser());
        register(TokenType.TRUE, new IdentParser());
        register(TokenType.FALSE, new IdentParser());
        register(TokenType.NIL, new IdentParser());
        register(TokenType.IDENT, new IdentParser());
        register(TokenType.SUB, new PrefixOperatorParser(13));
        register(TokenType.BANG, new PrefixOperatorParser(13));
        register(TokenType.LPAREN, new ParenParser());

        // infix

        register(TokenType.LPAREN, new CallParser(12));
        register(TokenType.MUL, new BinOperatorParser(11));
        register(TokenType.DIV, new BinOperatorParser(11));
        register(TokenType.MOD, new BinOperatorParser(11));
        register(TokenType.ADD, new BinOperatorParser(10));
        register(TokenType.SUB, new BinOperatorParser(10));
        register(TokenType.LT, new BinOperatorParser(5));
        register(TokenType.GT, new BinOperatorParser(5));
        register(TokenType.LE, new BinOperatorParser(5));
        register(TokenType.GE, new BinOperatorParser(5));
        register(TokenType.EQUAL, new BinOperatorParser(5));
        register(TokenType.NOTEQUAL, new BinOperatorParser(5));
        register(TokenType.AND, new BinOperatorParser(4));
        register(TokenType.OR, new BinOperatorParser(3));
        register(TokenType.ASSIGN, new AssignParser(2));
    }

    public static void register(TokenType type, PrefixParser prefixParser) {
        mPrefixParseLets.put(type, prefixParser);
    }

    public static void register(TokenType type, InfixParser infixParser) {
        mInfixParseLets.put(type, infixParser);
    }
}
