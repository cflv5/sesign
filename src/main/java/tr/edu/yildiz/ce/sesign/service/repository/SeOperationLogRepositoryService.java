package tr.edu.yildiz.ce.sesign.service.repository;

import java.util.List;

import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.context.TenantContext;
import tr.edu.yildiz.ce.sesign.domain.entity.SeOperationLog;
import tr.edu.yildiz.ce.sesign.repository.SeOperationLogRepository;

@Service
public class SeOperationLogRepositoryService {
    private final SeOperationLogRepository logRepository;

    public SeOperationLogRepositoryService(SeOperationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public SeOperationLog save(String message) {
        return logRepository.save(SeOperationLog.generate(message));
    }

    public List<SeOperationLog> getTenantsLog() {
        return logRepository.findByTenantId(TenantContext.getCurrentTenant().getRequestId());
    }

}
