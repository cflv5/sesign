package tr.edu.yildiz.ce.sesign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tr.edu.yildiz.ce.sesign.domain.entity.SeCertificate;

@Repository
public interface SeCertificateRepository extends JpaRepository<SeCertificate, String> {
    List<SeCertificate> findByTenantId(String tenantId);
}
