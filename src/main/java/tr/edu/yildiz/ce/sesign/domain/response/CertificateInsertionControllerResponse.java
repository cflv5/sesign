package tr.edu.yildiz.ce.sesign.domain.response;

import java.io.Serializable;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;

public class CertificateInsertionControllerResponse implements Serializable {
    private ResponseHeader responseHeader;
    private String certificateId;

    public CertificateInsertionControllerResponse(ResponseHeader responseHeader, String certificateId) {
        this.responseHeader = responseHeader;
        this.certificateId = certificateId;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

}
