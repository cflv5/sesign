package tr.edu.yildiz.ce.sesign.domain.dto;

import java.io.Serializable;

import tr.edu.yildiz.ce.sesign.domain.entity.SeCertificate;

public class SeCertificateDto implements Serializable {
    private String name;
    private String certificateId;
    private String tenantId;

    public SeCertificateDto() {
        super();
    }

    public SeCertificateDto(String name, String certificateId, String tenantId) {
        this.name = name;
        this.certificateId = certificateId;
        this.tenantId = tenantId;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public static SeCertificateDto of(SeCertificate c) {
        return new SeCertificateDto(c.getName(), c.getId(), c.getTenantId());
    }

}
