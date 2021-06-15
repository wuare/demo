package top.wuare.json.parser;

import top.wuare.json.exception.CommonException;
import top.wuare.json.lexer.JsonLexer;
import top.wuare.json.lexer.Token;

/**
 * JSON Parser
 *
 * @author wuare
 * @date 2021/6/15
 */
public class JsonParser {

    private JsonLexer lexer;

    private Token curToken;

    public void parse(String text) {
        lexer = new JsonLexer(text);
        curToken = lexer.nextToken();
        parseValue();
    }

    private void parseObject() {
        eat(Token.LBRACE);
        for (;;) {
            if (curToken == null) {
                throw new CommonException("parse object error");
            }
            if (curToken.getType() == Token.RBRACE) {
                eat(Token.RBRACE);
                break;
            }
            eat(Token.STRING);
            eat(Token.COLON);
            parseValue();
            if (curToken.getType() == Token.RBRACE) {
                eat(Token.RBRACE);
                break;
            }
            eat(Token.COMMA);
        }
    }

    private void parseArray() {
        eat(Token.LBRACKET);
        for (;;) {
            if (curToken == null) {
                throw new CommonException("parse array error");
            }
            if (curToken.getType() == Token.RBRACKET) {
                eat(Token.RBRACKET);
                break;
            }
            parseValue();
            if (curToken.getType() == Token.RBRACKET) {
                eat(Token.RBRACKET);
                break;
            }
            eat(Token.COMMA);
        }
    }

    private void parseValue() {
        if (curToken == null) {
            return;
        }
        switch (curToken.getType()) {
            case Token.LBRACE:
                // object start
                parseObject();
                break;
            case Token.LBRACKET:
                // array start
                parseArray();
                break;
            case Token.STRING:
            case Token.NUMBER:
            case Token.LITERAL_TRUE:
            case Token.LITERAL_FALSE:
            case Token.LITERAL_NULL:
                next();
                break;
            default:
                throw new CommonException("unexpected token: " + curToken);
        }
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
