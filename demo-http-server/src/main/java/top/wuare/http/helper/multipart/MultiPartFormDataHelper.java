package top.wuare.http.helper.multipart;

import top.wuare.http.define.Constant;
import top.wuare.http.exception.MultiPartFormException;
import top.wuare.http.proto.HttpRequest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MultiPartFormDataHelper {

    private static final String TEXT_CONTENT_DISPOSITION = "Content-Disposition";
    private static final String TEXT_CONTENT_TYPE = "Content-Type";
    private static final String TEXT_FILE_NAME = "filename";
    private static final String TEXT_NAME = "name";
    private static final int threshold = 1024 * 10;
    private static final String tempFilePath = System.getProperty("java.io.tmpdir");

    public static MultiPartFormData handle(HttpRequest request) throws MultiPartFormException {

        String contentType = request.getHeader(Constant.HTTP_HEADER_CONTENT_TYPE);
        if (contentType == null) {
            throw new MultiPartFormException("can't found content-type header");
        }
        if (!contentType.startsWith("multipart/form-data")) {
            throw new MultiPartFormException("content-type is not multipart/form-data");
        }
        String boundaryValue = getHeaderSubValue(contentType, "boundary=");
        if (boundaryValue == null) {
            throw new MultiPartFormException("can't find boundary");
        }
        try {
            InputStream in = request.getInputStream();
            String firstBoundary = readLine(in);
            if (!firstBoundary.equals("--" + boundaryValue)) {
                throw new MultiPartFormException("parse error, first boundary value not equals value in content type");
            }
            MultiPartFormData formData = new MultiPartFormData();
            parseMultiPartItem(in, formData, boundaryValue);
            return formData;
        } catch (IOException e) {
            throw new MultiPartFormException("", e);
        }
    }

    private static void parseMultiPartItem(InputStream in, MultiPartFormData formData, String boundaryValue)
            throws IOException, MultiPartFormException {
        do {
            parseHeaderAndData(in, formData, boundaryValue);
        } while (!parseLastBoundary(in));
    }

    private static boolean parseLastBoundary(InputStream in) throws IOException {
        String line = readLine(in);
        return line.equals("--");
    }

    private static void parseHeaderAndData(InputStream in, MultiPartFormData formData, String boundaryValue) throws IOException, MultiPartFormException {
        boundaryValue = "--" + boundaryValue;
        byte[] boundaryBytes = boundaryValue.getBytes(StandardCharsets.UTF_8);
        Map<String, String> headerMap = new HashMap<>();
        String line;
        while ((line = readLine(in)).length() != 0) {
            String[] split = line.split(":");
            if (split.length != 2) {
                throw new MultiPartFormException("解析multipart part中的header错误");
            }
            if (TEXT_CONTENT_DISPOSITION.equals(split[0].trim())) {
                String name = getHeaderSubValue(split[1], "name=");
                name = name.substring(1, name.length() - 1);
                headerMap.put(TEXT_NAME, name);
                String fileName = getHeaderSubValue(split[1], "filename=");
                if (fileName != null) {
                    headerMap.put(TEXT_FILE_NAME, fileName.substring(1, fileName.length() - 1));
                }
            }
            if (TEXT_CONTENT_TYPE.equals(split[0].trim())) {
                headerMap.put(split[0].trim(), split[1].trim());
            }
        }
        // 文件
        if (headerMap.get(TEXT_FILE_NAME) != null) {
            MultiPartFileItem fileItem = new MultiPartFileItem();
            fileItem.setName(headerMap.get(TEXT_NAME));
            fileItem.setFileName(headerMap.get(TEXT_FILE_NAME));
            fileItem.setContentType(headerMap.get(TEXT_CONTENT_TYPE));
            // 解析内容
            byte[] buf = readFixedData(in, boundaryBytes);
            // 10KB 说明没有读取完 写入到临时文件
            if (buf.length >= threshold) {
                File file = new File(tempFilePath + UUID.randomUUID());
                OutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buf);
                do {
                    buf = readFixedData(in, boundaryBytes);
                    fileOutputStream.write(buf);
                } while (buf.length >= threshold);
                fileOutputStream.close();
                fileItem.setContentLength(file.length());
                fileItem.setTempFile(file);
                fileItem.setOnDisk(true);
            } else {
                fileItem.setBuffer(buf);
                fileItem.setContentLength(buf.length);
                fileItem.setOnDisk(false);
            }
            formData.addFileItem(fileItem);
        } else {
            ByteArrayOutputStream arrayBuf = new ByteArrayOutputStream();
            int len;
            do {
                byte[] buf = readFixedData(in, boundaryBytes);
                len = buf.length;
                arrayBuf.write(buf);
            } while (len >= threshold);
            MultiPartTextItem item = new MultiPartTextItem();
            item.setName(headerMap.get(TEXT_NAME));
            item.setValue(new String(arrayBuf.toByteArray(), StandardCharsets.UTF_8));
            formData.addTextItem(item);
        }
    }

    private static String readLine(InputStream in) throws IOException {
        int c = in.read();
        int last = -1;
        StringBuilder builder = new StringBuilder();
        while (c != -1) {
            if (last == '\r' && c == '\n') {
                builder.setLength(builder.length() - 1);
                break;
            }
            builder.append((char) c);
            last = c;
            c = in.read();
        }
        return builder.toString();
    }

    private static byte[] readFixedData(InputStream in, byte[] boundary) throws IOException, MultiPartFormException {
        byte[] buf = new byte[boundary.length];
        fill(in, buf, 0, buf.length);
        int pos = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int c = buf[pos];
        while (true) {
            if (c == '\r') {
                System.arraycopy(buf, pos, buf, 0, buf.length - pos);
                fill(in, buf, buf.length - pos, pos);
                pos = 0;
                if (buf[pos + 1] == '\n') {
                    System.arraycopy(buf, 2, buf, 0, buf.length - 2);
                    fill(in, buf, buf.length - 2, 2);
                    if (!Arrays.equals(buf, boundary)) {
                        out.write('\r');
                        out.write('\n');
                        c = buf[pos];
                        continue;
                    } else {
                        break;
                    }
                }
            }
            out.write(c);
            pos++;
            if (pos >= buf.length) {
                if (out.size() >= threshold) {
                    break;
                }
                fill(in, buf, 0, buf.length);
                pos = 0;
            }
            c = buf[pos];
        }

        return out.toByteArray();
    }

    private static void fill(InputStream in, byte[] buf, int srcPos, int len) throws IOException {
        in.read(buf, srcPos, len);
    }

    private static String getHeaderSubValue(String value, String name) {
        String[] split = value.split(";");
        String subValue = null;
        for (String s : split) {
            if (s.trim().startsWith(name)) {
                subValue = s.trim().substring(name.length());
                break;
            }
        }
        return subValue;
    }
}
