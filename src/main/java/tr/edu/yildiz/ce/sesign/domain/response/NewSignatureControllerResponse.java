package tr.edu.yildiz.ce.sesign.domain.response;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.sesign.domain.dto.SeSignatureDto;

public class NewSignatureControllerResponse {
    private ResponseHeader responseHeader;
    private SeSignatureDto signature;

    public NewSignatureControllerResponse() {
        super();
    }

    public NewSignatureControllerResponse(ResponseHeader responseHeader, SeSignatureDto signature) {
        this.responseHeader = responseHeader;
        this.signature = signature;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public SeSignatureDto getSignature() {
        return signature;
    }

    public void setSignature(SeSignatureDto signature) {
        this.signature = signature;
    }

}
