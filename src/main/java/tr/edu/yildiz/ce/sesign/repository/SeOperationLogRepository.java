package tr.edu.yildiz.ce.sesign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tr.edu.yildiz.ce.sesign.domain.entity.SeOperationLog;

@Repository
public interface SeOperationLogRepository extends JpaRepository<SeOperationLog, Integer> {
    List<SeOperationLog> findByTenantIdOrderByIdDesc(String id);
}
