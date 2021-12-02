package top.wuare.lang.lexer;

import org.junit.Assert;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testNumber() {
        Lexer lexer = new Lexer("123");
        Token token = lexer.nextToken();
        Assert.assertSame(token.getType(), TokenType.NUMBER);
    }

    @Test
    public void testString() {
        String text = "\"123\"";
        Lexer lexer = new Lexer(text);
        Token token = lexer.nextToken();
        Assert.assertSame(token.getType(), TokenType.STRING);
        Assert.assertEquals("\"" + token.getText() + "\"", text);
    }

    @Test
    public void testVar() {
        Lexer lexer = new Lexer("var");
        Token token = lexer.nextToken();
        Assert.assertSame(token.getType(), TokenType.VAR);
    }
}
