package top.wuare.http.handler.request;

import top.wuare.http.define.HttpStatus;
import top.wuare.http.handler.RequestHandler;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

/**
 * 404 handler
 */
public class NotFoundRequestHandler implements RequestHandler {

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setBody(HttpStatus.NOT_FOUND.getReasonPhrase());
    }
}
