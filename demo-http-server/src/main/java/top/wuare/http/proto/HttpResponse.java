package top.wuare.http.proto;

import top.wuare.http.define.HttpStatus;
import top.wuare.http.exception.HttpServerException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * http response
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpResponse {

    private Socket socket;
    private OutputStream out;
    private HttpMessage httpMessage = new HttpMessage();
    private volatile boolean flushed = false;
    private boolean needFlush = true;

    public HttpResponse() {
    }

    public HttpResponse(Socket socket, OutputStream out) {
        this.socket = socket;
        this.out = out;
    }

    public HttpResponse(Socket socket, OutputStream out, HttpMessage httpMessage) {
        this.socket = socket;
        this.out = out;
        this.httpMessage = httpMessage;
    }

    public void addHeader(String key, String value) {
        Optional<HttpHeader> header = httpMessage.getHeaders().stream().filter(v -> v.getKey().equals(key)).findFirst();
        if (header.isPresent()) {
            header.get().setValue(value);
        } else {
            httpMessage.getHeaders().add(new HttpHeader(key, value));
        }
    }

    public String getHeader(String key) {
        Optional<HttpHeader> header = httpMessage.getHeaders().stream().filter(v -> v.getKey().equals(key)).findFirst();
        return header.map(HttpHeader::getValue).orElse(null);
    }

    public void setBody(String body) {
        HttpBody httpBody = new HttpBody(body.getBytes());
        httpMessage.setBody(httpBody);
    }

    public void setBody(byte[] body) {
        HttpBody httpBody = new HttpBody(body);
        httpMessage.setBody(httpBody);
    }

    public HttpResponse setStatus(int status, String statusDesc) {
        HttpResponseLine line = new HttpResponseLine(status, statusDesc);
        httpMessage.setHttpLine(line);
        return this;
    }

    public void setStatus(HttpStatus httpStatus) {
        HttpResponseLine line = new HttpResponseLine(httpStatus.getValue(), httpStatus.getReasonPhrase());
        httpMessage.setHttpLine(line);
    }

    public void flush() {

        if (socket == null || socket.isClosed()) {
            return;
        }
        if (!needFlush) {
            return;
        }
        if (flushed) {
            return;
        }
        flushed = true;
        HttpResponseLine httpLine = (HttpResponseLine) httpMessage.getHttpLine();
        byte[] data = httpMessage.getBody().getData();
        List<HttpHeader> headers = httpMessage.getHeaders();
        addHeader("Content-Length", String.valueOf(data.length));

        try {
            writeResponseLine(httpLine, out);
            writeResponseHeaders(headers, out);
            out.write(data);
            out.flush();
        } catch (Exception e) {
            throw new HttpServerException(e);
        }
    }

    public void writeResponseLine(HttpResponseLine httpLine, OutputStream out) throws IOException {
        String httpLineText = httpLine.getVersion() + " "
                + httpLine.getStatus() + " "
                + httpLine.getStatusDesc() + "\r\n";
        out.write(httpLineText.getBytes());
    }

    public void writeResponseHeaders(List<HttpHeader> headers, OutputStream out) throws IOException {
        StringBuilder headerBuilder = new StringBuilder();
        headers.forEach(v -> headerBuilder.append(v.getKey()).append(":").append(v.getValue()).append("\r\n"));
        headerBuilder.append("\r\n");
        out.write(headerBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void writeLineAndHeaders() throws IOException {
        writeResponseLine((HttpResponseLine) httpMessage.getHttpLine(), out);
        writeResponseHeaders(httpMessage.getHeaders(), out);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public HttpMessage getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(HttpMessage httpMessage) {
        this.httpMessage = httpMessage;
    }

    public boolean isFlushed() {
        return flushed;
    }

    public void setFlushed(boolean flushed) {
        this.flushed = flushed;
    }

    public void setNeedFlush(boolean needFlush) {
        this.needFlush = needFlush;
    }

    public boolean isNeedFlush() {
        return needFlush;
    }
}
