package top.wuare.json.lexer;

import org.junit.Assert;
import org.junit.Test;
import top.wuare.json.exception.CommonException;

/**
 * test JSON Lexer
 *
 * @author wuare
 * @date 2021/6/15
 */
public class JsonLexerTest {

    @Test
    public void testNextCh() {
        JsonLexer lexer = new JsonLexer("a");
        Assert.assertEquals('a', lexer.getCh());
        lexer.nextCh();
        Assert.assertEquals(-1, lexer.getCh());
    }

    @Test(expected = CommonException.class)
    public void testNextTokenUnExpectedCharacter() {
        JsonLexer lexer = new JsonLexer("a");
        lexer.nextToken();
    }

    @Test
    public void testNextTokenExpectedCharacter() {
        JsonLexer lexer = new JsonLexer("{}");
        Token token0 = lexer.nextToken();
        Assert.assertNotNull(token0);
        Assert.assertEquals("{", token0.getVal());

        Token token1 = lexer.nextToken();
        Assert.assertNotNull(token1);
        Assert.assertEquals("}", token1.getVal());

        Token token2 = lexer.nextToken();
        Assert.assertNull(token2);
    }

    @Test
    public void testNextTokenTrue() {
        JsonLexer lexer = new JsonLexer("true");
        Token token0 = lexer.nextToken();
        Assert.assertNotNull(token0);
        Assert.assertEquals("true", token0.getVal());
    }

    @Test
    public void testNextTokenFalse() {
        JsonLexer lexer = new JsonLexer("false");
        Token token0 = lexer.nextToken();
        Assert.assertNotNull(token0);
        Assert.assertEquals("false", token0.getVal());
    }

    @Test
    public void testNextTokenNull() {
        JsonLexer lexer = new JsonLexer("null");
        Token token0 = lexer.nextToken();
        Assert.assertNotNull(token0);
        Assert.assertEquals("null", token0.getVal());
    }

    @Test
    public void testNextToken() {
        JsonLexer lexer = new JsonLexer("{ \"orderCode\": \"C001\", \"type\": 1, \"pageNo\": 3 , \"amount\": -1.236 } ");
        Token token;
        while ((token = lexer.nextToken()) != null) {
            System.out.println(token);
        }
    }

    @Test
    public void testNextTokenEscapeCharacter() {
        String t = "{ \"name\": \"\\t\\nbob\" }";
        JsonLexer lexer = new JsonLexer(t);
        lexer.nextToken(); // {
        lexer.nextToken(); // name
        lexer.nextToken(); // :
        Token token = lexer.nextToken(); // expect value
        String str = token.getVal();

        Assert.assertEquals(str, "\t\nbob");
    }

    @Test
    public void testNextTokenEscapeCharacter0() {
        String t = "{ \"name\": \"\t\nbob\" }";
        JsonLexer lexer = new JsonLexer(t);
        lexer.nextToken(); // {
        lexer.nextToken(); // name
        lexer.nextToken(); // :
        Token token = lexer.nextToken(); // expect value
        String str = token.getVal();

        Assert.assertEquals(str, "\t\nbob");
    }

    @Test
    public void testNextTokenNumber() {
        JsonLexer lexer0 = new JsonLexer("-0");
        Token token0 = lexer0.nextToken();
        Assert.assertNotNull(token0);
        Assert.assertEquals(TokenType.NUMBER, token0.getType());

        JsonLexer lexer1 = new JsonLexer("12");
        Token token1 = lexer1.nextToken();
        Assert.assertNotNull(token1);
        Assert.assertEquals(TokenType.NUMBER, token1.getType());

        JsonLexer lexer2 = new JsonLexer("-12");
        Token token2 = lexer2.nextToken();
        Assert.assertNotNull(token2);
        Assert.assertEquals(TokenType.NUMBER, token2.getType());

        JsonLexer lexer3 = new JsonLexer("-12.345");
        Token token3 = lexer3.nextToken();
        Assert.assertNotNull(token3);
        Assert.assertEquals(TokenType.NUMBER, token3.getType());

        JsonLexer lexer4 = new JsonLexer("-12.345e+2");
        Token token4 = lexer4.nextToken();
        Assert.assertNotNull(token4);
        Assert.assertEquals(TokenType.NUMBER, token4.getType());

        JsonLexer lexer5 = new JsonLexer("1.0e-2");
        Token token5 = lexer5.nextToken();
        Assert.assertNotNull(token5);
        Assert.assertEquals(TokenType.NUMBER, token5.getType());

        JsonLexer lexer6 = new JsonLexer("0");
        Token token6 = lexer6.nextToken();
        Assert.assertNotNull(token6);
        Assert.assertEquals(TokenType.NUMBER, token6.getType());
    }
}
