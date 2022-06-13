package tr.edu.yildiz.ce.sesign.domain.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class NewSignatureControllerRequest implements Serializable {
    @NotBlank
    private String fileId;
    @NotBlank
    private String certificateId;
    @NotBlank
    private String certificatePassword;
    @NotBlank
    private String fileHash;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

}
