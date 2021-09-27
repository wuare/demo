package top.wuare.http.helper;

import top.wuare.http.proto.HttpHeader;
import top.wuare.http.proto.HttpResponse;
import top.wuare.http.proto.HttpResponseLine;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResponseHelper {

    private final HttpResponse response;
    private final OutputStream originOutputStream;
    private boolean commit = false;
    private volatile boolean flushed = false;

    public ResponseHelper(HttpResponse response, OutputStream originOutputStream) {
        this.response = response;
        this.originOutputStream = originOutputStream;
    }

    public void prepareResponse() throws IOException {
        if (commit) {
            return;
        }
        HttpResponseLine httpLine = (HttpResponseLine) response.getHttpMessage().getHttpLine();
        if (!"chunked".equals(response.getHeader("Transfer-Encoding"))) {
            if (response.getContentLength() == -1) {
                response.setContentLength(0);
            }
            response.addHeader("Content-Length", Long.toString(response.getContentLength()));
        }

        List<HttpHeader> headers = response.getHttpMessage().getHeaders();
        writeResponseLine(httpLine, originOutputStream);
        writeResponseHeaders(headers, originOutputStream);
        commit = true;
    }


    public void flush() throws IOException {

        if (response.getSocket() == null || response.getSocket().isClosed()) {
            return;
        }
        if (flushed) {
            return;
        }
        flushed = true;
        prepareResponse();
        if ("chunked".equals(response.getHeader("Transfer-Encoding"))) {
            response.getOutputStream().flush();
            originOutputStream.write("0\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        } else {
            byte[] data = response.getHttpMessage().getBody().getData();
            if (response.getContentLength() > -1 && data != null) {
                originOutputStream.write(data);
            }
        }
        originOutputStream.flush();
    }

    private void writeResponseLine(HttpResponseLine httpLine, OutputStream out) throws IOException {
        String httpLineText = httpLine.getVersion() + " "
                + httpLine.getStatus() + " "
                + httpLine.getStatusDesc() + "\r\n";
        out.write(httpLineText.getBytes());
    }

    private void writeResponseHeaders(List<HttpHeader> headers, OutputStream out) throws IOException {
        StringBuilder headerBuilder = new StringBuilder();
        headers.forEach(v -> headerBuilder.append(v.getKey()).append(":").append(v.getValue()).append("\r\n"));
        headerBuilder.append("\r\n");
        out.write(headerBuilder.toString().getBytes(StandardCharsets.UTF_8));
    }
}
