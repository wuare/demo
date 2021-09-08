package top.wuareb.highlight;

import org.junit.Test;
import top.wuareb.highlight.lexer.java.Lexer;
import top.wuareb.highlight.lexer.java.Token;

import java.io.*;

/**
 *
 */
public class TestLexer {

    @Test
    public void testToken0() {
        String text = "/**\n" +
                "     * test method\n" +
                "     * \n" +
                "     * @author wuare\n" +
                "     */\n" +
                "    public void test() {\n" +
                "        int a = 123;\n" +
                "        String b = \"this is a message!\";\n" +
                "        // print something\n" +
                "        System.out.println(b);\n" +
                "    }";
        genHtml(text);
    }

    @Test
    public void testProp() {
        String dir = System.getProperty("user.dir");
        System.out.println(dir);
    }

    @Test
    public void testToken1() throws IOException {

        String dir = System.getProperty("user.dir");
        String filePath = dir + File.separator + "src" + File.separator + "main"
                + File.separator + "java" + File.separator + "top" + File.separator
                + "wuareb" + File.separator + "highlight" + File.separator
                + "lexer" + File.separator + "Lexer.java";
        System.out.println("File Path: [" + filePath + "]");
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File Not Found");
            return;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream in = new FileInputStream(file)) {
            int n;
            byte[] buf = new byte[4096];
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
        }
        String text = out.toString();

        genHtml(text);
    }

    private void genHtml(String text) {
        Lexer lexer = new Lexer(text);
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n");
        builder.append("<head></head>\n");
        builder.append("<body>\n");
        builder.append("<pre style=\"background-color: #2B2B2B; color: white; padding: 20px 20px;\">\n");
        Token token;
        while ((token = lexer.nextToken()) != null) {
            // #698652
            if (token.getType() == Token.STRING_LITERAL) {
                String newText = "<span style=\"color: #698652;\">\"" + token.getValue() + "\"</span>";
                builder.append(newText);
                continue;
            }
            // #698652
            if (token.getType() == Token.CHAR_LITERAL) {
                String charText;
                if (token.getValue().charAt(0) == '\n') {
                    charText = "\\ n";
                } else if (token.getValue().charAt(0) == '\r') {
                    charText = "\\ r";
                } else {
                    charText = token.getValue();
                }
                String newText = "<span style=\"color: #698652;\">'" + charText + "'</span>";
                builder.append(newText);
                continue;
            }
            // #698652
            if (token.getType() == Token.COMMENT) {
                String newText = "<span style=\"color: #698652;\">" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #808080
            if (token.getType() == Token.LINE_COMMENT) {
                String newText = "<span style=\"color: #808080;\">" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #CC7832
            if (token.getType() == Token.KEY_WORD) {
                String newText = "<span style=\"color: #CC7832;\">" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #5596BA
            if (token.getType() == Token.NUMBER) {
                String newText = "<span style=\"color: #5596BA;\">" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #FFEF28
            if (token.getType() == Token.LBRACE || token.getType() == Token.RBRACE) {
                String newText = "<span style=\"color: #FFEF28;\">" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #BBB529
            if (token.getType() == Token.AT) {
                String newText = "<span style=\"color: #BBB529;\">" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            builder.append(token.getValue());
        }

        builder.append("\n</pre>\n");
        builder.append("</body>");
        System.out.println(builder);
    }
}
