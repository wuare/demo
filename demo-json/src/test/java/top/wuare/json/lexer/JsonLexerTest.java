package top.wuare.json.lexer;

import org.junit.Assert;
import org.junit.Test;

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
        lexer.nextCh();
        Assert.assertEquals('a', lexer.getCh());
        lexer.nextCh();
        Assert.assertEquals(-1, lexer.getCh());
    }
}
