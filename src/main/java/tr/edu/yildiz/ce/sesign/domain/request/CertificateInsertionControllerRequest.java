package tr.edu.yildiz.ce.sesign.domain.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class CertificateInsertionControllerRequest implements Serializable {
    @NotBlank
    private String password;

    public CertificateInsertionControllerRequest() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
