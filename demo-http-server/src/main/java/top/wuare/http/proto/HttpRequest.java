package top.wuare.http.proto;

import top.wuare.http.exception.HttpInputStreamReadException;
import top.wuare.http.exception.HttpParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * http request
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpRequest {

    private Socket socket;
    private InputStream in;
    private HttpMessage httpMessage;

    public HttpRequest() {
    }

    public HttpRequest(Socket socket, InputStream in) {
        this.socket = socket;
        this.in = in;
    }

    public HttpRequest(Socket socket, InputStream in, HttpMessage httpMessage) {
        this.socket = socket;
        this.in = in;
        this.httpMessage = httpMessage;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public HttpMessage getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(HttpMessage httpMessage) {
        this.httpMessage = httpMessage;
    }

    public String getHeader(String key) {
        return httpMessage.getHeaders().stream()
                .filter(v -> v.getKey().equals(key))
                .findFirst().map(HttpHeader::getValue).orElse(null);
    }

    public String getBody() {
        byte[] buf = new byte[4096];
        ByteArrayOutputStream arrayBuf = new ByteArrayOutputStream();
        try {
            int c;
            while ((c = in.read(buf)) != -1) {
                arrayBuf.write(buf, 0, c);
            }
            return arrayBuf.size() > 0 ? new String(arrayBuf.toByteArray(), StandardCharsets.UTF_8) : null;
        } catch (IOException e) {
            throw new HttpInputStreamReadException(e);
        }
    }

    public byte[] getOriginalBody() {
        byte[] buf = new byte[4096];
        ByteArrayOutputStream arrayBuf = new ByteArrayOutputStream();
        try {
            int c;
            while ((c = in.read(buf)) != -1) {
                arrayBuf.write(buf, 0, c);
            }
            return arrayBuf.size() > 0 ? arrayBuf.toByteArray() : null;
        } catch (IOException e) {
            throw new HttpInputStreamReadException(e);
        }
    }

    public HttpRequest addHeader(String key, String value) {
        for (HttpHeader header : httpMessage.getHeaders()) {
            if (header.getKey().equals(key)) {
                header.setValue(value);
                return this;
            }
        }
        httpMessage.getHeaders().add(new HttpHeader(key, value));
        return this;
    }

    public String getUrl() {
        return ((HttpRequestLine) httpMessage.getHttpLine()).getUrl();
    }
}
