package tr.edu.yildiz.ce.sesign.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tr.edu.yildiz.ce.sesign.domain.entity.SeSignature;

@Repository
public interface SeSignatureRepository extends JpaRepository<SeSignature, String> {
    List<SeSignature> findAllByTenantId(String tenantId);
}
