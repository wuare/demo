package top.wuare.lang.lexer;

import java.io.*;

public class Lexer {

    private final Reader reader;
    private int ch;
    private int line = 1;
    private int column;

    public Lexer(String text) {
        reader = new StringReader(text);
    }

    public Lexer(InputStream in) {
        reader = new InputStreamReader(in);
    }

    public Token nextToken() {

        return null;
    }

    public void advance() {
        try {
            ch = reader.read();
            if (ch == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
