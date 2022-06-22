package tr.edu.yildiz.ce.sesign.domain.response;

import java.io.Serializable;
import java.util.List;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.sesign.domain.entity.SeOperationLog;

public class TenantLogsControllerResponse implements Serializable {
    private ResponseHeader responseHeader;
    private List<SeOperationLog> logs;

    public TenantLogsControllerResponse() {
        super();
    }

    public TenantLogsControllerResponse(ResponseHeader responseHeader, List<SeOperationLog> logs) {
        this.responseHeader = responseHeader;
        this.logs = logs;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<SeOperationLog> getLogs() {
        return logs;
    }

    public void setLogs(List<SeOperationLog> logs) {
        this.logs = logs;
    }

}
