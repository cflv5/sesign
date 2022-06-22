package tr.edu.yildiz.ce.sesign.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import tr.edu.yildiz.ce.se.base.context.TenantContext;

@Entity
public class SeOperationLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String tenantId;

    @Column
    private String requestId;

    @Column
    private String messaage;

    public SeOperationLog() {
        super();
    }

    public static SeOperationLog generate(String message) {
        var log = new SeOperationLog();
        log.setMessaage(message);
        log.setTenantId(TenantContext.getCurrentTenant().getTenantId());
        log.setRequestId(TenantContext.getCurrentTenant().getRequestId());
        return log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessaage() {
        return messaage;
    }

    public void setMessaage(String messaage) {
        this.messaage = messaage;
    }

}
