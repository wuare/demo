package top.wuareb.highlight.gen.html.java;

import top.wuareb.highlight.gen.Gen;
import top.wuareb.highlight.lexer.java.JavaLexer;
import top.wuareb.highlight.lexer.java.JavaToken;

public class JavaGen implements Gen {

    @Override
    public String gen(String text) {
        JavaLexer lexer = new JavaLexer(text);
        StringBuilder builder = new StringBuilder();
        JavaToken token;
        while ((token = lexer.nextToken()) != null) {
            // #698652
            if (token.getType() == JavaToken.STRING_LITERAL) {
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
            if (token.getType() == JavaToken.CHAR_LITERAL) {
                String charText;
                char tmp = token.getValue().length() > 0 ? token.getValue().charAt(0) : ' ';
                if (tmp == '\n') {
                    charText = "\\n";
                } else if (tmp == '\r') {
                    charText = "\\r";
                } else if (tmp == '\t') {
                    charText = "\\t";
                } else {
                    charText = token.getValue();
                }
                String newText = "<span class='hl-char'>'" + charText + "'</span>";
                builder.append(newText);
                continue;
            }
            // #698652
            if (token.getType() == JavaToken.COMMENT) {
                String newText = "<span class='hl-cmt'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #808080
            if (token.getType() == JavaToken.LINE_COMMENT) {
                String newText = "<span class='hl-line-cmt'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #CC7832
            if (token.getType() == JavaToken.KEY_WORD) {
                String newText = "<span class='hl-key'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #5596BA
            if (token.getType() == JavaToken.NUMBER) {
                String newText = "<span class='hl-num'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #FFEF28
            if (token.getType() == JavaToken.LBRACE || token.getType() == JavaToken.RBRACE) {
                String newText = "<span class='hl-brace'>" + token.getValue() + "</span>";
                builder.append(newText);
                continue;
            }
            // #BBB529
            if (token.getType() == JavaToken.AT) {
                JavaToken nextToken = lexer.nextToken();
                if (nextToken != null && nextToken.getType() == JavaToken.IDENTIFIER) {
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
            if (token.getType() == JavaToken.LT) {
                builder.append("&lt;");
                continue;
            }
            if (token.getType() == JavaToken.GT) {
                builder.append("&gt;");
                continue;
            }
            if (token.getType() == JavaToken.LE) {
                builder.append("&lt;=");
                continue;
            }
            if (token.getType() == JavaToken.GE) {
                builder.append("&gt;=");
                continue;
            }
            if (token.getType() == JavaToken.L_SHIFT_ASSIGN) {
                builder.append("&lt;&lt;=");
                continue;
            }
            if (token.getType() == JavaToken.R_SHIFT_ASSIGN) {
                builder.append("&gt;&gt;=");
                continue;
            }
            if (token.getType() == JavaToken.U_R_SHIFT_ASSIGN) {
                builder.append("&gt;&gt;&gt;=");
                continue;
            }
            if (token.getType() == JavaToken.L_SHIFT) {
                builder.append("&lt;&lt;");
                continue;
            }
            if (token.getType() == JavaToken.R_SHIFT) {
                builder.append("&gt;&gt;");
                continue;
            }
            builder.append(token.getValue());
        }
        return builder.toString();
    }
}
