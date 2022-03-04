package top.wuareb.highlight.lexer.markdown;

import org.junit.Test;

public class MdLexerTest {

    @Test
    public void testH1() {
        MdLexer lexer = new MdLexer("# aa # a");
        System.out.println(lexer.nextToken());
        System.out.println(lexer.nextToken());
    }

    @Test
    public void testAlink() {
        MdLexer lexer = new MdLexer("[x](h)");
        System.out.println(lexer.nextToken());
        System.out.println(lexer.nextToken());
    }

    @Test
    public void testImg() {
        MdLexer lexer = new MdLexer("!a[x](h)");
        System.out.println(lexer.nextToken());
        System.out.println(lexer.nextToken());
        System.out.println(lexer.nextToken());
        System.out.println(lexer.nextToken());
    }
}
