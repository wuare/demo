package top.wuare.http.builder;

import top.wuare.http.define.Constant;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

public class StaticResourcePathBuilder {

    public String buildPath(Properties properties) throws IOException {

        if (properties == null) {
            return null;
        }
        String configType = properties.getProperty(Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE,
                Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE_CLASSPATH);
        String configPath = properties.getProperty(Constant.CONFIG_STATIC_RESOURCE_PATH);
        if (configPath != null) {
            char c = configPath.charAt(configPath.length() - 1);
            if (c != '/') {
                configPath = configPath + "/";
            }
        }
        if (Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE_ABSOLUTE.equals(configType)) {
            return configPath;
        } else if (Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE_RELATIVE.equals(configType)) {
            String path = null;
            URL resource = Thread.currentThread().getContextClassLoader().getResource("");
            if (resource != null) {
                path = resource.getPath();
            } else {
                File f = new File(".");
                path = f.getCanonicalPath();
            }
            if (!path.startsWith("file:")) {
                path = "file:" + path;
                path = new URL(path).getPath();
            }
            path = URLDecoder.decode(path, "UTF-8");
            if (path.charAt(path.length() - 1) != '/') {
                path = path + "/";
            }
            return path + configPath;
        }
        return null;
    }
}
