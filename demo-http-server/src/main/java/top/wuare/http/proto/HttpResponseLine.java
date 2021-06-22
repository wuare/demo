package top.wuare.http.proto;

/**
 * response line
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpResponseLine extends HttpLine {

    private String version;
    private int status;
    private String statusDesc;

    public HttpResponseLine() {
    }

    public HttpResponseLine(int status, String statusDesc) {
        this.version = HttpLine.DEFAULT_VERSION;
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public HttpResponseLine(String version, int status, String statusDesc) {
        this.version = version;
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
