package tr.edu.yildiz.ce.sesign.domain.request;

import javax.validation.constraints.NotBlank;

public class SignatureVerificationControllerRequest {
    @NotBlank
    private String fileId;
    @NotBlank
    private String fileHash;
    @NotBlank
    private String signatureId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }

}
