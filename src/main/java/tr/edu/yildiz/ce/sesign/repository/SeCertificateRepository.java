package tr.edu.yildiz.ce.sesign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tr.edu.yildiz.ce.sesign.domain.entity.SeCertificate;

@Repository
public interface SeCertificateRepository extends JpaRepository<SeCertificate, String> {
    @Query(value = "SELECT c FROM SeCertificate c WHERE c.tenantId = ?1 AND c.status='ACTIVE'")
    List<SeCertificate> findByTenantId(String tenantId);
}
