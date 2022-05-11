package top.wuareb.highlight.lexer;

import top.wuareb.highlight.exception.LexerException;

import java.io.IOException;
import java.io.StringReader;

public abstract class Lexer<T extends Token> {

    private int ch;
    private int line = 1;
    private int col;
    private final StringReader reader;

    public Lexer(String text) {
        this.reader = new StringReader(text);
    }

    abstract T nextToken();

    public void advance() {
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
