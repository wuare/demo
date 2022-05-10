package top.wuareb.highlight.lexer.json;

import org.junit.Assert;
import org.junit.Test;

public class JsonTextTest {

    @Test
    public void testString() {
        String c = "\"a\"";
        JsonLexer lexer = new JsonLexer(c);
        JsonToken t = lexer.nextToken();
        Assert.assertEquals(JsonTokenType.STRING, t.getType());
    }

    @Test
    public void testNumber() {
        String c = "1";
        JsonLexer lexer = new JsonLexer(c);
        JsonToken t = lexer.nextToken();
        Assert.assertEquals(JsonTokenType.NUMBER, t.getType());
    }

    @Test
    public void testTrue() {
        String c = "true";
        JsonLexer lexer = new JsonLexer(c);
        JsonToken t = lexer.nextToken();
        Assert.assertEquals(JsonTokenType.TRUE, t.getType());
    }

    @Test
    public void testFalse() {
        String c = "false";
        JsonLexer lexer = new JsonLexer(c);
        JsonToken t = lexer.nextToken();
        Assert.assertEquals(JsonTokenType.FALSE, t.getType());
    }

    @Test
    public void testAll() {
        String c = "{\"name\": \"wuare\", \"age\": 28, \"fat\": false, \"other\": null}";
        JsonLexer lexer = new JsonLexer(c);
        JsonToken t;
        while ((t = lexer.nextToken()).getType() != JsonTokenType.EOF) {
            System.out.println(t);
        }
    }
}
