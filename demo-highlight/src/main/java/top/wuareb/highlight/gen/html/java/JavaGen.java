package top.wuareb.highlight.gen.html.java;

import top.wuareb.highlight.gen.Gen;
import top.wuareb.highlight.lexer.java.Lexer;
import top.wuareb.highlight.lexer.java.Token;

public class JavaGen implements Gen {

    @Override
    public String gen(String text) {
        Lexer lexer = new Lexer(text);
        StringBuilder builder = new StringBuilder();
        Token token;
        while ((token = lexer.nextToken()) != null) {
            // #698652
            if (token.getType() == Token.STRING_LITERAL) {
                String s = token.getValue()
                        .replaceAll("\n", "\\\\n")
                        .replaceAll("\t", "\\\\t")
                        .replaceAll("\r", "\\\\r")
                        .replaceAll("\"", "\\\\\"")
                        .replaceAll("<", "&lt;");
                String newText = "<span class='hl-str'>\"" + s + "\"</span>";
                builder.append(newText);
                continue;
            }
            // #698652
            if (token.getType() == Token.CHAR_LITERAL) {
                String charText;
                if (token.getValue().charAt(0) == '\n') {
                    charText = "\\n";
                } else if (token.getValue().charAt(0) == '\r') {
                    charText = "\\r";
                } else if (token.getValue().charAt(0) == '\t') {
                    charText = "\\t";
                } else {
                    charText = token.getValue();
                }
                String newText = "<span class='hl-char'>'" + charText + "'</span>";
                builder.append(newText);
                continue;
            }
            // #698652
            if (token.getType() == Token.COMMENT) {
                String newText = "<span class='hl-cmt'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #808080
            if (token.getType() == Token.LINE_COMMENT) {
                String newText = "<span class='hl-line-cmt'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #CC7832
            if (token.getType() == Token.KEY_WORD) {
                String newText = "<span class='hl-key'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #5596BA
            if (token.getType() == Token.NUMBER) {
                String newText = "<span class='hl-num'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #FFEF28
            if (token.getType() == Token.LBRACE || token.getType() == Token.RBRACE) {
                String newText = "<span class='hl-brace'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #BBB529
            if (token.getType() == Token.AT) {
                Token nextToken = lexer.nextToken();
                if (nextToken != null && nextToken.getType() == Token.IDENTIFIER) {
                    String newText = "<span class='hl-anno'>" + token.getValue() + nextToken.getValue() + "</span>";
                    builder.append(newText);
                } else {
                    String newText = "<span class='hl-anno'>" + token.getValue() + "</span>";
                    builder.append(newText);
                    if (nextToken != null) {
                        builder.append("<span class='hl-anno'>").append(nextToken.getValue()).append("</span>");
                    }
                }

                continue;
            }
            if (token.getType() == Token.LT) {
                builder.append("&lt;");
                continue;
            }
            if (token.getType() == Token.GT) {
                builder.append("&gt;");
                continue;
            }
            if (token.getType() == Token.LE) {
                builder.append("&lt;=");
                continue;
            }
            if (token.getType() == Token.GE) {
                builder.append("&gt;=");
                continue;
            }
            if (token.getType() == Token.L_SHIFT_ASSIGN) {
                builder.append("&lt;&lt;=");
                continue;
            }
            if (token.getType() == Token.R_SHIFT_ASSIGN) {
                builder.append("&gt;&gt;=");
                continue;
            }
            if (token.getType() == Token.U_R_SHIFT_ASSIGN) {
                builder.append("&gt;&gt;&gt;=");
                continue;
            }
            if (token.getType() == Token.L_SHIFT) {
                builder.append("&lt;&lt;");
                continue;
            }
            if (token.getType() == Token.R_SHIFT) {
                builder.append("&gt;&gt;");
                continue;
            }
            builder.append(token.getValue());
        }
        return builder.toString();
    }
}
