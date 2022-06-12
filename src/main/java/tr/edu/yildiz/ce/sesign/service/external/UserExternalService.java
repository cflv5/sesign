package tr.edu.yildiz.ce.sesign.service.external;

import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.service.SeRestService;
import tr.edu.yildiz.ce.sesign.domain.dto.TenantUser;

@Service
public class UserExternalService {
    private final SeRestService seRestService;

    public UserExternalService(SeRestService seRestService) {
        this.seRestService = seRestService;
    }

    public TenantUser fetchTenantUser(String tenantId) {
        var user = new TenantUser();
        user.setName("Bedirhan");
        user.setSurname("Akcay");
        return user;
    }
}
