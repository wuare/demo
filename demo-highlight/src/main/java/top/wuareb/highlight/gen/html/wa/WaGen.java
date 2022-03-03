package top.wuareb.highlight.gen.html.wa;

import top.wuareb.highlight.gen.Gen;
import top.wuareb.highlight.lexer.wa.WaLexer;
import top.wuareb.highlight.lexer.wa.WaToken;
import top.wuareb.highlight.lexer.wa.WaTokenType;

public class WaGen implements Gen {

    @Override
    public String gen(String text) {
        WaLexer lexer = new WaLexer(text);
        StringBuilder builder = new StringBuilder();
        WaToken token;
        while ((token = lexer.nextToken()).getType() != WaTokenType.EOF) {
            System.out.println(token);
            switch (token.getType()) {
                case STRING:
                    builder.append("<span class='hl-str'>").append("\"").append(token.getText()).append("\"").append("</span>");
                    break;
                case NUMBER:
                    builder.append("<span class='hl-num'>").append(token.getText()).append("</span>");
                    break;
                case VAR:
                case FUNC:
                case IF:
                case WHILE:
                case RETURN:
                case ELSE:
                    builder.append("<span class='hl-kwd'>").append(token.getText()).append("</span>");
                    break;
                case COMMENT:
                    builder.append("<span class='hl-cmt'>").append(token.getText()).append("</span>");
                    break;
                case IDENT:
                    builder.append("<span class='hl-idt'>").append(token.getText()).append("</span>");
                    break;
                default:
                    builder.append(token.getText());
            }
        }
        return builder.toString();
    }
}
