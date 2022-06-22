package tr.edu.yildiz.ce.sesign.service;

import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.sesign.domain.response.TenantLogsControllerResponse;
import tr.edu.yildiz.ce.sesign.service.repository.SeOperationLogRepositoryService;

@Service
public class OperationLogControllerService {
    private final SeOperationLogRepositoryService logRepositoryService;

    public OperationLogControllerService(SeOperationLogRepositoryService logRepositoryService) {
        this.logRepositoryService = logRepositoryService;
    }

    public TenantLogsControllerResponse fetchTenantsLogs() {
        return new TenantLogsControllerResponse(ResponseHeader.success(),
                logRepositoryService.getTenantsLog());
    }

}
