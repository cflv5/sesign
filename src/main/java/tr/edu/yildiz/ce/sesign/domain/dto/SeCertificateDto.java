package tr.edu.yildiz.ce.sesign.domain.dto;

import java.io.Serializable;

public class SeCertificateDto implements Serializable {
    private String name;
    private String certificateId;

    public SeCertificateDto() {
        super();
    }

    public SeCertificateDto(String name, String certificateId) {
        this.name = name;
        this.certificateId = certificateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

}
