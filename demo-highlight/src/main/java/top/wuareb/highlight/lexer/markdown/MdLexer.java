package top.wuareb.highlight.lexer.markdown;

import top.wuareb.highlight.exception.LexerException;

import java.io.IOException;
import java.io.StringReader;

public class MdLexer {
    private int ch;
    private int line = 1;
    private int column;
    private final StringReader reader;

    public MdLexer(String text) {
        this.reader = new StringReader(text);
        advance();
    }

    public MdToken nextToken() {
        if (ch == -1) {
            return null;
        }
        return null;
    }

    private void advance() {
        try {
            ch = reader.read();
            if (ch == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        } catch (IOException e) {
            throw new LexerException(e);
        }
    }
}
