package tr.edu.yildiz.ce.sesign.domain.response;

import java.io.Serializable;
import java.util.List;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.sesign.domain.dto.SeCertificateDto;

public class FindTenantsCertificateControllerResponse implements Serializable {
    private ResponseHeader responseHeader;
    private List<SeCertificateDto> certificates;

    public FindTenantsCertificateControllerResponse(ResponseHeader responseHeader,
            List<SeCertificateDto> certificates) {
        this.responseHeader = responseHeader;
        this.certificates = certificates;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<SeCertificateDto> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<SeCertificateDto> certificates) {
        this.certificates = certificates;
    }

}
