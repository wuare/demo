package top.wuare.http.handler;

import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;

/**
 * request handler
 *
 * @author wuare
 * @date 2021/6/22
 */
@FunctionalInterface
public interface RequestHandler {

    void handle(HttpRequest request, HttpResponse response);
}
