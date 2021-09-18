package top.wuare.http.util;

public class HttpUtil {

    public static String getQueryParamUrl(String url) {
        if (url == null) {
            return null;
        }
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c == '?') {
                return url.substring(i + 1);
            }
        }
        return null;
    }

    public static String getUrlWithOutQueryParam(String url) {
        if (url == null) {
            return null;
        }
        for (int i = 0; i < url.length(); i++) {
            char c = url.charAt(i);
            if (c == '?') {
                return url.substring(0, i);
            }
        }
        return url;
    }
}
