package top.wuare.http.config;

public class Config {

    // static resource path type, classpath is default
    // relative/absolute/classpath
    private String staticResourcePathType;

    // static resource path
    private String staticResourcePath;

    public String getStaticResourcePathType() {
        return staticResourcePathType;
    }

    public void setStaticResourcePathType(String staticResourcePathType) {
        this.staticResourcePathType = staticResourcePathType;
    }

    public String getStaticResourcePath() {
        return staticResourcePath;
    }

    public void setStaticResourcePath(String staticResourcePath) {
        this.staticResourcePath = staticResourcePath;
    }

}
