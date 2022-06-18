package tr.edu.yildiz.ce.sesign.domain.response;

import java.io.Serializable;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.sesign.domain.dto.SeCertificateDto;

public class FetchSeCertificateDetailControllerResponse implements Serializable {
    private ResponseHeader responseHeader;
    private SeCertificateDto certificate;

    public FetchSeCertificateDetailControllerResponse() {
    }

    public FetchSeCertificateDetailControllerResponse(ResponseHeader responseHeader, SeCertificateDto certificate) {
        this.responseHeader = responseHeader;
        this.certificate = certificate;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public SeCertificateDto getCertificate() {
        return certificate;
    }

    public void setCertificate(SeCertificateDto certificate) {
        this.certificate = certificate;
    }

}
