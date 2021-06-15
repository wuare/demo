package top.wuare.json.parser;

import top.wuare.json.exception.CommonException;
import top.wuare.json.lexer.JsonLexer;
import top.wuare.json.lexer.Token;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JSON Parser
 *
 * @author wuare
 * @date 2021/6/15
 */
public class JsonParser {

    private JsonLexer lexer;

    private Token curToken;

    public Object parse(String text) {
        lexer = new JsonLexer(text);
        curToken = lexer.nextToken();
        return parseValue();
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> map = new HashMap<>();
        eat(Token.LBRACE);
        for (;;) {
            if (curToken == null) {
                throw new CommonException("parse object error");
            }
            if (curToken.getType() == Token.RBRACE) {
                eat(Token.RBRACE);
                break;
            }
            String key = curToken.getVal().substring(1, curToken.getVal().length() - 1);
            eat(Token.STRING);
            eat(Token.COLON);
            Object value = parseValue();
            map.put(key, value);
            if (curToken.getType() == Token.RBRACE) {
                eat(Token.RBRACE);
                break;
            }
            eat(Token.COMMA);
        }
        return map;
    }

    private List<Object> parseArray() {
        List<Object> list = new ArrayList<>();
        eat(Token.LBRACKET);
        for (;;) {
            if (curToken == null) {
                throw new CommonException("parse array error");
            }
            if (curToken.getType() == Token.RBRACKET) {
                eat(Token.RBRACKET);
                break;
            }
            list.add(parseValue());
            if (curToken.getType() == Token.RBRACKET) {
                eat(Token.RBRACKET);
                break;
            }
            eat(Token.COMMA);
        }
        return list;
    }

    private Object parseValue() {
        if (curToken == null) {
            return null;
        }
        Object obj;
        switch (curToken.getType()) {
            case Token.LBRACE:
                // object start
                obj = parseObject();
                break;
            case Token.LBRACKET:
                // array start
                obj = parseArray();
                break;
            case Token.STRING:
                obj = curToken.getVal().substring(1, curToken.getVal().length() - 1);
                next();
                break;
            case Token.NUMBER:
                obj = new BigDecimal(curToken.getVal());
                next();
                break;
            case Token.LITERAL_TRUE:
                obj = Boolean.TRUE;
                next();
                break;
            case Token.LITERAL_FALSE:
                obj = Boolean.FALSE;
                next();
                break;
            case Token.LITERAL_NULL:
                obj = null;
                next();
                break;
            default:
                throw new CommonException("unexpected token: " + curToken);
        }
        return obj;
    }

    private void next() {
        curToken = lexer.nextToken();
    }

    private void eat(int type) {
        if (type == curToken.getType()) {
            next();
            return;
        }
        throw new CommonException("expect token type: " + type + ", but get type: "
                + curToken.getType() + ", current token information: " + curToken);
    }

}
