package top.wuare.http.handler.request;

import top.wuare.http.define.HttpStatus;
import top.wuare.http.handler.RequestHandler;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

public class MethodOptionsRequestHandler implements RequestHandler {

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.NO_CONTENT);
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET");
        response.addHeader("Access-Control-Allow-Origin", "*");
    }
}
