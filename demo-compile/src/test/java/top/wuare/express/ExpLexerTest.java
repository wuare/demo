package top.wuare.express;

import org.junit.Test;

public class ExpLexerTest {

    @Test
    public void testNextToken() {
        String text = "aaa 123";
        ExpLexer lexer = new ExpLexer(text);
        ExpToken token;
        while ((token = lexer.nextToken()) != null) {
            System.out.println(token);
        }
    }
}
