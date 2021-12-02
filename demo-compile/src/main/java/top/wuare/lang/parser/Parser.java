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
                return parseDeclareStmt();
            case IF:
                return parseIfStmt();
            case WHILE:
                return parseWhileStmt();
            case RETURN:
                return parseReturnStmt();
            default:
                return parseExprStmt();
        }
    }

    // DeclareStmt: VAR ident (= Expr)? ';'
    private Stmt parseDeclareStmt() {
        DeclareStmt stmt = new DeclareStmt();
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

    // IfStmt: IF '(' Expr ')' '{' Block '}' (ELSE '{' Block '}')?
    private Stmt parseIfStmt() {
        IfStmt stmt = new IfStmt();
        eat(TokenType.IF);
        eat(TokenType.LPAREN);
        Expr expr = parseExp(0);
        stmt.setExpr(expr);
        eat(TokenType.RPAREN);
        eat(TokenType.LBRACE);
        stmt.setThen(parseBlock());
        eat(TokenType.RBRACE);
        if (curToken.getType() == TokenType.ELSE) {
            consume();
            eat(TokenType.LBRACE);
            stmt.setEls(parseBlock());
            eat(TokenType.RBRACE);
        }
        return stmt;
    }

    private Stmt parseWhileStmt() {
        return null;
    }

    private Stmt parseReturnStmt() {
        return null;
    }

    private Stmt parseExprStmt() {
        return null;
    }


    public Expr parseExp(int precedence) {
        if (curToken == null) {
            return null;
        }
        PrefixParser prefixParser = mPrefixParseLets.get(curToken.getType());
        if (prefixParser == null) {
            throw new RuntimeException("could not parse '" + curToken.getType().getText() + "'");
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

    public void consume() {
        curToken = lexer.nextToken();
    }

    public void want(TokenType type) {
        if (type == curToken.getType()) {
            return;
        }
        throw new RuntimeException("expect token type: " + type.getText() + ", but get type: "
                + curToken.getType().getText() + ", at line: " + curToken.getLine() + ", column: " + curToken.getColumn());
    }

    private void eat(TokenType type) {
        if (type == curToken.getType()) {
            consume();
            return;
        }
        throw new RuntimeException("expect token type: " + type.getText() + ", but get type: "
                + curToken.getType().getText() + ", at line: " + curToken.getLine() + ", column: " + curToken.getColumn());
    }

    static {
        register(TokenType.NUMBER, new IdentParser());
        register(TokenType.IDENT, new IdentParser());
        register(TokenType.SUB, new PrefixOperatorParser(12));
        register(TokenType.BANG, new PrefixOperatorParser(12));
        register(TokenType.LPAREN, new ParenParser());

        // infix
        register(TokenType.ADD, new BinOperatorParser(10));
        register(TokenType.SUB, new BinOperatorParser(10));
        register(TokenType.MUL, new BinOperatorParser(11));
        register(TokenType.DIV, new BinOperatorParser(11));
        register(TokenType.MOD, new BinOperatorParser(11));
        register(TokenType.LT, new BinOperatorParser(3));
        register(TokenType.GT, new BinOperatorParser(3));
        register(TokenType.LE, new BinOperatorParser(3));
        register(TokenType.GE, new BinOperatorParser(3));
        register(TokenType.EQUAL, new BinOperatorParser(3));
        register(TokenType.NOTEQUAL, new BinOperatorParser(3));
        register(TokenType.AND, new BinOperatorParser(2));
        register(TokenType.OR, new BinOperatorParser(1));
        register(TokenType.ASSIGN, new AssignParser(0));
    }

    public static void register(TokenType type, PrefixParser prefixParser) {
        mPrefixParseLets.put(type, prefixParser);
    }

    public static void register(TokenType type, InfixParser infixParser) {
        mInfixParseLets.put(type, infixParser);
    }
}
