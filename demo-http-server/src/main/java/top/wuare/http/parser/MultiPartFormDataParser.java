package top.wuare.http.parser;

import top.wuare.http.exception.MultiPartFormException;
import top.wuare.http.helper.multipart.MultiPartFormData;
import top.wuare.http.proto.HttpRequest;

public class MultiPartFormDataParser {

    private static final String TEXT_CONTENT_DISPOSITION = "Content-Disposition";
    private static final String TEXT_CONTENT_TYPE = "Content-Type";
    private static final String TEXT_FILE_NAME = "filename";
    private static final String TEXT_NAME = "name";
    private static final int threshold = 1024 * 10;
    private static final String tempFilePath = System.getProperty("java.io.tmpdir");

    public static MultiPartFormData handle(HttpRequest request) throws MultiPartFormException {
        return null;
    }
}
