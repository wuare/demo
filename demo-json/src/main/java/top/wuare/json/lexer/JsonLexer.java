package top.wuare.json.lexer;

import top.wuare.json.exception.CommonException;
import top.wuare.json.util.IOUtil;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * JSON Lexer
 *
 * @author wuare
 * @date 2021/6/15
 */
public class JsonLexer {

    private int ch;
    private final Reader reader;

    public JsonLexer(String text) {
        this.reader = new StringReader(text);
    }

    protected void nextCh() {
        try {
            ch = reader.read();
        } catch (IOException e) {
            IOUtil.close(reader);
            throw new CommonException(e);
        }
    }

    public Token nextToken() {
        nextCh();
        if (ch == -1) {
            return null;
        }
        while (Character.isSpaceChar(ch)) {
            nextCh();
        }
        return null;
    }

    public int getCh() {
        return ch;
    }
}
