package top.wuare.http.proto;

import top.wuare.http.define.Constant;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.helper.ResponseHelper;
import top.wuare.http.io.HttpOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * http response
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpResponse {

    private final Socket socket;
    private final OutputStream outputStream;
    private HttpMessage httpMessage = new HttpMessage();
    private long contentLength = -1;
    private final ResponseHelper responseHelper;

    public HttpResponse(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new HttpOutputStream(this, socket.getOutputStream());
        this.responseHelper = new ResponseHelper(this, socket.getOutputStream());
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
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        setBody(bytes);
    }

    public void setBody(byte[] body) {
        HttpBody httpBody = new HttpBody(body);
        httpMessage.setBody(httpBody);
        if (body != null) {
            setContentLength(body.length);
        }
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

    public Socket getSocket() {
        return socket;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public HttpMessage getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(HttpMessage httpMessage) {
        this.httpMessage = httpMessage;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getContentLength() {
        return contentLength;
    }

    public ResponseHelper getResponseHelper() {
        return responseHelper;
    }

    public String getContentType() {
        return getHeader(Constant.HTTP_HEADER_CONTENT_TYPE);
    }

    public void setContentType(String contentType) {
        addHeader(Constant.HTTP_HEADER_CONTENT_TYPE, contentType);
    }
}
