package tr.edu.yildiz.ce.sesign.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tr.edu.yildiz.ce.sesign.domain.entity.SeCertificate;

@Repository
public interface SeCertificateRepository extends JpaRepository<SeCertificate, String> {

}
