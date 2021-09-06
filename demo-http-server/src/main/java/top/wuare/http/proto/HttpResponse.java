package top.wuare.http.proto;

import top.wuare.http.define.HttpStatus;
import top.wuare.http.exception.HttpServerException;
import top.wuare.http.util.IOUtil;

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

    public HttpResponse addHeader(String key, String value) {
        Optional<HttpHeader> header = httpMessage.getHeaders().stream().filter(v -> v.getKey().equals(key)).findFirst();
        if (header.isPresent()) {
            header.get().setValue(value);
        } else {
            httpMessage.getHeaders().add(new HttpHeader(key, value));
        }
        return this;
    }

    public HttpResponse setBody(String body) {
        HttpBody httpBody = new HttpBody(body.getBytes());
        httpMessage.setBody(httpBody);
        return this;
    }

    public HttpResponse setBody(byte[] body) {
        HttpBody httpBody = new HttpBody(body);
        httpMessage.setBody(httpBody);
        return this;
    }

    public HttpResponse setStatus(int status, String statusDesc) {
        HttpResponseLine line = new HttpResponseLine(status, statusDesc);
        httpMessage.setHttpLine(line);
        return this;
    }

    public HttpResponse setStatus(HttpStatus httpStatus) {
        HttpResponseLine line = new HttpResponseLine(httpStatus.getValue(), httpStatus.getReasonPhrase());
        httpMessage.setHttpLine(line);
        return this;
    }

    public void flush() {
        if (flushed) {
            return;
        }
        flushed = true;
        HttpResponseLine httpLine = (HttpResponseLine) httpMessage.getHttpLine();
        byte[] data = httpMessage.getBody().getData();
        List<HttpHeader> headers = httpMessage.getHeaders();
        addHeader("Content-Length", String.valueOf(data.length));
        // addHeader("Connection", "close");
        try {
            writeResponseLine(httpLine, out);
            writeResponseHeaders(headers, out);
            out.write(data);
            out.flush();
        } catch (Exception e) {
            throw new HttpServerException(e);
        }
        // finally {
        //     IOUtil.close(out);
        //     IOUtil.close(socket);
        // }
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
}
