package tr.edu.yildiz.ce.sesign.domain.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class SeSignature {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Lob
    private byte[] signature;

    @ManyToOne(fetch = FetchType.LAZY)
    private SeCertificate cert;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    public SeSignature() {
        super();
    }

    public SeSignature(byte[] signature, SeCertificate cert) {
        this.signature = signature;
        this.cert = cert;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public SeCertificate getCert() {
        return cert;
    }

    public void setCert(SeCertificate cert) {
        this.cert = cert;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
