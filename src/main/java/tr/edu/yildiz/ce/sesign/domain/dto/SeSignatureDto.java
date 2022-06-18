package tr.edu.yildiz.ce.sesign.domain.dto;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import org.bouncycastle.util.encoders.Base64;

import tr.edu.yildiz.ce.sesign.domain.entity.SeSignature;

public class SeSignatureDto implements Serializable {
    private String id;
    private String certificateId;
    private String fileId;
    private String signature;
    private OffsetDateTime createdAt;

    public static SeSignatureDto of(SeSignature signature) {
        var dto = new SeSignatureDto();
        dto.id = signature.getId();
        dto.certificateId = signature.getCert().getId();
        dto.createdAt = signature.getCreatedAt();
        dto.fileId = signature.getFileId();
        dto.signature = new String(Base64.encode(signature.getSignature()), StandardCharsets.UTF_8);
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getSignature() {
        return signature;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
