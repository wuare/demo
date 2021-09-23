package top.wuare.http.parser;

import top.wuare.http.exception.HttpReadTimeOutException;
import top.wuare.http.exception.HttpRequestClosedException;
import top.wuare.http.proto.HttpBody;
import top.wuare.http.proto.HttpHeader;
import top.wuare.http.proto.HttpLine;
import top.wuare.http.proto.HttpMessage;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpRequestLine;
import top.wuare.http.exception.HttpParserException;
import top.wuare.http.util.HttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * http message parser
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpMessageParser {

    private static final Logger logger = Logger.getLogger(HttpMessageParser.class.getName());

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
                    throw new HttpRequestClosedException("parse request line error, no data for read");
                }
                if (!Character.isWhitespace(ch)) {
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
            requestLine.setQueryParam(HttpUtil.getQueryParamUrl(url.toString()));
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
        } catch (SocketTimeoutException e) {
            throw new HttpReadTimeOutException(e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "parse request line error", e.getMessage());
            throw new HttpParserException("parse request line error, can not read data from inputStream");
        }
    }

    private void want(int ch, int expect) {
        if (ch != expect) {
            throw new HttpParserException("expect " + (char) expect + ", but get " + (char) ch);
        }
    }

    public List<HttpHeader> parseRequestHeaders(InputStream in) {
        if (in == null) {
            throw new HttpParserException("parse request header error, the inputStream is null");
        }
        try {
            List<HttpHeader> httpHeaders = new ArrayList<>();
            for (;;) {
                int ch = in.read();
                if (ch == '\r') {
                    ch = in.read();
                    if (ch == '\n') {
                        break;
                    }
                    throw new HttpParserException("parse request header error, syntax error");
                }
                // parse key
                StringBuilder key = new StringBuilder();
                for (;;) {
                    if (ch == -1) {
                        throw new HttpParserException("parse request header error, no data for read");
                    }
                    if (ch == ':') {
                        break;
                    }
                    key.append((char) ch);
                    ch = in.read();
                }
                // consume ':'
                ch = in.read();
                StringBuilder value = new StringBuilder();
                // parse value
                for (;;) {
                    if (ch == -1) {
                        throw new HttpParserException("parse request header error, no data for read");
                    }
                    if (ch == '\r') {
                        break;
                    }
                    value.append((char) ch);
                    ch = in.read();
                }
                // consume '\n'
                ch = in.read();
                want(ch, '\n');
                httpHeaders.add(new HttpHeader(key.toString().trim(), value.toString().trim()));
            }
            return httpHeaders;
        } catch (SocketTimeoutException e) {
            throw new HttpReadTimeOutException(e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "parse request header error", e);
            throw new HttpParserException("parse request header error, can not read data from inputStream");
        }
    }

    public HttpBody parseRequestBody(InputStream in, int contentLength) {
        if (contentLength <= 0) {
            return new HttpBody(new byte[0]);
        }
        byte[] buf = new byte[4096];
        ByteArrayOutputStream arrayBuf = new ByteArrayOutputStream();
        try {
            int c = 0;
            while (c < contentLength) {
                int read = in.read(buf);
                arrayBuf.write(buf, 0, read);
                c = c + read;
            }
            return new HttpBody(arrayBuf.toByteArray());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "parse request body error", e);
            throw new HttpParserException(e);
        }
    }

    public HttpRequest parseRequest(InputStream in) {
        HttpRequestLine httpLine = (HttpRequestLine) parseRequestLine(in);
        List<HttpHeader> httpHeaders = parseRequestHeaders(in);
        HttpMessage httpMessage = new HttpMessage(httpLine, httpHeaders);
        if ("POST".equals(httpLine.getMethod())) {
            int length = httpHeaders.stream()
                    .filter(v -> "Content-Length".equals(v.getKey()))
                    .findFirst().map(v -> Integer.parseInt(v.getValue())).orElse(0);
            HttpBody httpBody = parseRequestBody(in, length);
            httpMessage.setBody(httpBody);
        }
        return new HttpRequest(null, in, httpMessage);
    }

}
