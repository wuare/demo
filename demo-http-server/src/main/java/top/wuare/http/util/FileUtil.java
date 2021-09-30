package top.wuare.http.util;

import java.io.*;
import java.util.*;

public class FileUtil {

    public static byte[] fileToByteArray(File file) {
        if (file != null && file.exists() && file.isFile()) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[4096];
                int n;
                while ((n = in.read(buf)) != -1) {
                    out.write(buf, 0, n);
                }
                return out.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtil.close(in);
            }
        }
        return new byte[0];
    }

    public static void copyFile(File srcFile, File destFile) {
        if (srcFile == null || destFile == null) {
            throw new NullPointerException();
        }
        if (srcFile.exists() && srcFile.isFile()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(srcFile));
                out = new BufferedOutputStream(new FileOutputStream(destFile));
                byte[] buf = new byte[4096];
                int n;
                while ((n = in.read(buf)) != -1) {
                    out.write(buf, 0, n);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtil.close(in);
                IOUtil.close(out);
            }
        }
    }

    public static void writeByteArrayToFile(byte[] buffer, File destFile) {
        Objects.requireNonNull(destFile);
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(destFile));
            out.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.close(out);
        }
    }
}
