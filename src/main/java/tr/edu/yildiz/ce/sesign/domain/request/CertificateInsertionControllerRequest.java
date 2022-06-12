package tr.edu.yildiz.ce.sesign.domain.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class CertificateInsertionControllerRequest implements Serializable {
    @NotBlank
    private String password;
    @NotBlank
    private String name;

    public CertificateInsertionControllerRequest() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
