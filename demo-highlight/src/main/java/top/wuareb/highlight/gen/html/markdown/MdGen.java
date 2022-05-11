package top.wuareb.highlight.gen.html.markdown;

import top.wuareb.highlight.gen.Gen;
import top.wuareb.highlight.lexer.markdown.MdLexer;
import top.wuareb.highlight.lexer.markdown.MdToken;
import top.wuareb.highlight.lexer.markdown.MdTokenType;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MdGen implements Gen {

    private static final Logger logger = Logger.getLogger(Gen.class.getName());

    @Override
    public String gen(String text) {
        MdLexer lexer = new MdLexer(text);
        StringBuilder builder = new StringBuilder();
        MdToken token;
        while ((token = lexer.nextToken()).getType() != MdTokenType.EOF) {
            logger.log(Level.FINE, token.toString());
            switch (token.getType()) {
                case H1:
                    builder.append("<h1>").append(token.getText()).append("</h1>");
                    break;
                case H2:
                    builder.append("<h2>").append(token.getText()).append("</h2>");
                    break;
                case H3:
                    builder.append("<h3>").append(token.getText()).append("</h3>");
                    break;
                case H4:
                    builder.append("<h4>").append(token.getText()).append("</h4>");
                    break;
                case H5:
                    builder.append("<h5>").append(token.getText()).append("</h5>");
                    break;
                case H6:
                    builder.append("<h6>").append(token.getText()).append("</h6>");
                    break;
                case ALINK:
                    String link2 = token.getLink().substring(1, token.getLink().length() - 1);
                    String text2 = token.getText().substring(1, token.getText().length() - 1);
                    builder.append("<a href='").append(link2).append("'>").append(text2).append("</a>");
                    break;
                case IMG:
                    String link3 = token.getLink().substring(1, token.getLink().length() - 1);
                    String text3 = token.getText().substring(1, token.getText().length() - 1);
                    builder.append("<img alt='").append(text3).append("' src='").append(link3).append("'/>");
                    break;
                case NEW_LINE:
                    builder.append("</br>");
                    break;
                default:
                    builder.append(token.getText());
            }
        }
        return builder.toString();
    }
}
