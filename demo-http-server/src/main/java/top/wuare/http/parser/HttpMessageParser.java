package top.wuare.http.parser;

import top.wuare.http.proto.HttpLine;
import top.wuare.http.proto.HttpRequestLine;
import top.wuare.http.exception.HttpParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * http message parser
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpMessageParser {

    public HttpLine parseRequestLine(InputStream in) {
        if (in == null) {
            throw new HttpParserException("parse request line error, the inputStream is null");
        }
        HttpRequestLine requestLine = new HttpRequestLine();
        try {
            int ch = in.read();
            // skip white space
            for (;;) {
                if (ch == -1) {
                    throw new HttpParserException("parse request line error, no data for read");
                }
                if (!Character.isSpaceChar(ch)) {
                    break;
                }
                ch = in.read();
            }
            // parse method
            StringBuilder method = new StringBuilder();
            for (;;) {
                if (Character.isLetter(ch)) {
                    method.append((char) ch);
                    ch = in.read();
                    continue;
                }
                break;
            }
            requestLine.setMethod(method.toString());
            // space character
            want(ch, ' ');
            ch = in.read();
            // parse url
            StringBuilder url = new StringBuilder();
            for (;;) {
                if (ch == -1) {
                    throw new HttpParserException("parse request line error, no data for read");
                }
                if (ch == ' ') {
                    break;
                }
                url.append((char) ch);
                ch = in.read();
            }
            requestLine.setUrl(url.toString());
            // space character
            want(ch, ' ');
            ch = in.read();
            // version
            StringBuilder version = new StringBuilder();
            for (;;) {
                if (ch == -1) {
                    throw new HttpParserException("parse request line error, no data for read");
                }
                if (ch == '\r') {
                    break;
                }
                version.append((char) ch);
                ch = in.read();
            }
            requestLine.setVersion(version.toString());
            // consume '\n'
            ch = in.read();
            want(ch, '\n');
            return requestLine;
        } catch (IOException e) {
            throw new HttpParserException("parse request line error, can not read data from inputStream");
        }
    }

    private void want(int ch, int expect) {
        if (ch != expect) {
            throw new HttpParserException("expect " + (char) expect + ", but get " + (char) ch);
        }
    }
}
