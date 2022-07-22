package top.wuare.json.parser;

import top.wuare.json.exception.CommonException;
import top.wuare.json.lexer.JsonLexer;
import top.wuare.json.lexer.Token;
import top.wuare.json.lexer.TokenType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON Parser
 *
 * @author wuare
 * @since 2021/6/15
 */
public class JsonParser {

    private JsonLexer lexer;

    private Token curToken;

    // antlr4 json grammar
    // json: value
    //     ;
    // value: STRING
    //      | NUMBER
    //      | obj
    //      | arr
    //      | 'true'
    //      | 'false'
    //      | 'null
    //      ;
    // obj: '{' pair (',' pair)* '}'
    //    | '{' '}'
    //    ;
    // pair: STRING ':' value
    // arr: '[' value (',' value)* ']'
    //    | '[' ']'
    //    ;
    public Object parse(String text) {
        lexer = new JsonLexer(text);
        curToken = lexer.nextToken();
        Object o = parseValue();
        if (curToken != null) {
            throw new CommonException(errMsg(curToken));
        }
        return o;
    }

    // obj: '{' pair (',' pair)* '}'
    //    | '{' '}'
    //    ;
    // pair: STRING ':' value
    private Map<String, Object> parseObject() {
        Map<String, Object> map = new LinkedHashMap<>();
        eat(TokenType.LBRACE);
        if (curToken.getType() == TokenType.STRING) {
            String key = curToken.getVal();
            eat(TokenType.STRING);
            eat(TokenType.COLON);
            Object value = parseValue();
            map.put(key, value);
        }
        while (curToken.getType() == TokenType.COMMA) {
            eat(TokenType.COMMA);
            if (curToken.getType() != TokenType.STRING) {
                throw new CommonException("parse object error, " + errMsg(curToken));
            }
            String key = curToken.getVal();
            eat(TokenType.STRING);
            eat(TokenType.COLON);
            Object value = parseValue();
            map.put(key, value);
        }
        eat(TokenType.RBRACE);
        return map;
    }

    // arr: '[' value (',' value)* ']'
    //    | '[' ']'
    //    ;
    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        eat(TokenType.LBRACKET);
        if (curToken.getType() != TokenType.RBRACKET) {
            list.add(parseValue());
            while (curToken.getType() == TokenType.COMMA) {
                eat(TokenType.COMMA);
                list.add(parseValue());
            }
        }
        eat(TokenType.RBRACKET);
        return list;
    }

    private Object parseValue() {
        if (curToken == null) {
            return null;
        }
        Object obj;
        switch (curToken.getType()) {
            case LBRACE:
                // object start
                obj = parseObject();
                break;
            case LBRACKET:
                // array start
                obj = parseArray();
                break;
            case STRING:
                obj = curToken.getVal();
                next();
                break;
            case NUMBER:
                obj = new BigDecimal(curToken.getVal());
                next();
                break;
            case LITERAL_TRUE:
                obj = Boolean.TRUE;
                next();
                break;
            case LITERAL_FALSE:
                obj = Boolean.FALSE;
                next();
                break;
            case LITERAL_NULL:
                obj = null;
                next();
                break;
            default:
                throw new CommonException(errMsg(curToken));
        }
        return obj;
    }

    private void next() {
        curToken = lexer.nextToken();
    }

    private void eat(TokenType type) {
        if (type == curToken.getType()) {
            next();
            return;
        }
        throw new CommonException(errMsg(curToken) + ", except get type: "
                + type.getText() + ", but get: " + curToken.getType().getText());
    }

    private String errMsg(Token t) {
        return "unexpected token: '" + t.getVal() + "', at line: "
                + t.getLine() + ", column: " + t.getColumn();
    }
}
