package tr.edu.yildiz.ce.sesign.domain.external;

public class FileDto {
    private String name;
    private String hashValue;
    private String tenantId;
    private String fileId;
    private String contentType;
    private AccessType accessType;

    public FileDto() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public FileDto name(String name) {
        this.name = name;
        return this;
    }

    public FileDto tenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public FileDto fileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public FileDto contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

}