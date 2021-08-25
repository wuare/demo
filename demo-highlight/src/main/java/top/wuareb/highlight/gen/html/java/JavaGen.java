package top.wuareb.highlight.gen.html.java;

import top.wuareb.highlight.gen.Gen;
import top.wuareb.highlight.lexer.Lexer;
import top.wuareb.highlight.lexer.Token;

public class JavaGen implements Gen {

    @Override
    public String gen(String text) {
        Lexer lexer = new Lexer(text);
        StringBuilder builder = new StringBuilder();
//        builder.append("<!DOCTYPE html>\n");
//        builder.append("<head></head>\n");
//        builder.append("<body>\n");
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
            builder.append(token.getValue());
        }

        builder.append("\n</pre>\n");
//        builder.append("</body>");
        return builder.toString();
    }
}
