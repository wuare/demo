package top.wuareb.highlight.gen.html.json;

import top.wuareb.highlight.gen.Gen;
import top.wuareb.highlight.lexer.json.JsonLexer;
import top.wuareb.highlight.lexer.json.JsonToken;
import top.wuareb.highlight.lexer.json.JsonTokenType;

public class JsonGen implements Gen {

    @Override
    public String gen(String text) {
        JsonLexer lexer = new JsonLexer(text);
        StringBuilder builder = new StringBuilder();
        JsonToken token;
        while ((token = lexer.nextToken()).getType() != JsonTokenType.EOF) {
            switch (token.getType()) {
                case STRING:
                    JsonToken t = lexer.nextToken();
                    StringBuilder b0 = new StringBuilder();
                    while (t.getType() == JsonTokenType.WHITE_SPACE) {
                        b0.append(t.getValue());
                        t = lexer.nextToken();
                    }
                    lexer.setPeekToken(t);
                    if (t.getType() == JsonTokenType.COLON) {
                        builder.append("<span class='hl-key'>").append("\"").append(token.getValue()).append("\"").append("</span>");
                    } else {
                        builder.append("<span class='hl-str'>").append("\"").append(token.getValue()).append("\"").append("</span>");
                    }
                    builder.append(b0);
                    break;
                case NUMBER:
                    builder.append("<span class='hl-num'>").append(token.getValue()).append("</span>");
                    break;
                case TRUE:
                case FALSE:
                    builder.append("<span class='hl-bol'>").append(token.getValue()).append("</span>");
                    break;
                case NULL:
                    builder.append("<span class='hl-nil'>").append(token.getValue()).append("</span>");
                    break;
                default:
                    builder.append("<span class='hl-txt'>").append(token.getValue()).append("</span>");

            }
        }
        return builder.toString();
    }
}
