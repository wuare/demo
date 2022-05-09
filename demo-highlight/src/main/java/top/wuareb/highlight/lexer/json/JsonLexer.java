package top.wuareb.highlight.lexer.json;

import top.wuareb.highlight.exception.LexerException;

import java.io.IOException;
import java.io.StringReader;

public class JsonLexer {
    private int ch;
    private int line = 1;
    private int col;
    private final StringReader reader;

    public JsonLexer(String text) {
        reader = new StringReader(text);
        advance();
    }

    public JsonToken nextToken() {
        return null;
    }

    private void advance() {
        try {
            ch = reader.read();
            if (ch == '\n') {
                line++;
                col = 0;
            } else {
                col++;
            }
        } catch (IOException e) {
            throw new LexerException(e);
        }
    }
}
