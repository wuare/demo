package top.wuareb.highlight.gen.html.wa;

import top.wuareb.highlight.gen.Gen;
import top.wuareb.highlight.lexer.wa.WaLexer;
import top.wuareb.highlight.lexer.wa.WaToken;
import top.wuareb.highlight.lexer.wa.WaTokenType;
import top.wuareb.highlight.util.Sets;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WaGen implements Gen {

    private static final Logger logger = Logger.getLogger(WaGen.class.getName());

    private final Set<String> buildInFuncSet = Sets.of("print", "time", "len", "arrNew", "arrAdd", "strAt");

    @Override
    public String gen(String text) {
        WaLexer lexer = new WaLexer(text);
        StringBuilder builder = new StringBuilder();
        WaToken token;
        while ((token = lexer.nextToken()).getType() != WaTokenType.EOF) {
            logger.log(Level.FINE, token.toString());
            switch (token.getType()) {
                case STRING:
                    String s0 = token.getText().replaceAll("<", "&lt;");
                    builder.append("<span class='hl-str'>").append("\"").append(s0).append("\"").append("</span>");
                    break;
                case NUMBER:
                    builder.append("<span class='hl-num'>").append(token.getText()).append("</span>");
                    break;
                case VAR:
                case FUNC:
                case IF:
                case WHILE:
                case FOR:
                case BREAK:
                case RETURN:
                case ELSE:
                case TRUE:
                case FALSE:
                case NIL:
                    builder.append("<span class='hl-kwd'>").append(token.getText()).append("</span>");
                    break;
                case COMMENT:
                    builder.append("<span class='hl-cmt'>").append(token.getText()).append("</span>");
                    break;
                case IDENT:
                    String identName = token.getText();
                    WaToken t = lexer.nextToken();
                    if (buildInFuncSet.contains(identName) && t.getType() == WaTokenType.LPAREN) {
                        builder.append("<span class='hl-in-func'>").append(identName).append("</span>");
                    } else {
                        builder.append("<span class='hl-idt'>").append(identName).append("</span>");
                    }
                    builder.append(t.getText());
                    break;
                default:
                    builder.append(token.getText());
            }
        }
        return builder.toString();
    }
}
