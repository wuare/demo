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
        JsonLexer lexer = new JsonLexer("{ \"orderCode\": \"C001\", \"type\": 1, \"pageNo\": 3 , \"amount\": -1.236 }");
        Token token;
        while ((token = lexer.nextToken()) != null) {
            System.out.println(token);
        }
    }

    @Test
    public void testNextTokenEscapeCharacter() {
        // TODO
    }
}
