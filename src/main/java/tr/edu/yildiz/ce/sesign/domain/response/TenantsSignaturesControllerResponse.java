package tr.edu.yildiz.ce.sesign.domain.response;

import java.io.Serializable;
import java.util.List;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.sesign.domain.dto.SeSignatureDto;

public class TenantsSignaturesControllerResponse implements Serializable {
    private ResponseHeader responseHeader;
    private List<SeSignatureDto> signatures;

    public TenantsSignaturesControllerResponse() {
        super();
    }

    public TenantsSignaturesControllerResponse(ResponseHeader responseHeader, List<SeSignatureDto> signatures) {
        this.responseHeader = responseHeader;
        this.signatures = signatures;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<SeSignatureDto> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<SeSignatureDto> signatures) {
        this.signatures = signatures;
    }

}
