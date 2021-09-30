package top.wuare.http.helper.multipart;

import top.wuare.http.util.FileUtil;

import java.io.File;
import java.util.logging.Logger;

public class MultiPartFileItem {

    private static final Logger logger = Logger.getLogger(MultiPartFileItem.class.getName());

    private String name;
    private String fileName;
    private String contentType;
    private long contentLength;
    private File tempFile;
    private boolean onDisk;
    private byte[] buffer;

    public void transferToFile(File destFile) {
        if (onDisk) {
            FileUtil.copyFile(tempFile, destFile);
        } else {
            FileUtil.writeByteArrayToFile(buffer, destFile);
        }
    }

    public byte[] getContent() {
        return onDisk ? FileUtil.fileToByteArray(tempFile) : buffer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setOnDisk(boolean onDisk) {
        this.onDisk = onDisk;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }

    public void clear() {
        if (onDisk && tempFile != null) {
            boolean deleted = tempFile.delete();
            if (!deleted) {
                logger.warning("temp file not delete, path: [" + tempFile.getPath() + "]");
            }
        }
    }

    public File getTempFile() {
        return tempFile;
    }
}
