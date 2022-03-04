package top.wuareb.highlight.gen.html;

import org.junit.Test;
import top.wuareb.highlight.gen.html.markdown.MdGen;

public class MdGenTest {

    private final MdGen gen = new MdGen();

    @Test
    public void testHtml() {
        String s = "# 一级标题\n 我们知道\n这是一个链接[wuareb](https://wuareb.top)";

        System.out.println(gen.gen(s));
    }
}
