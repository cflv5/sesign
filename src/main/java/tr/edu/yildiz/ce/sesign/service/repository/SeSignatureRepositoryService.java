package tr.edu.yildiz.ce.sesign.service.repository;

import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.sesign.domain.entity.SeCertificate;
import tr.edu.yildiz.ce.sesign.domain.entity.SeSignature;
import tr.edu.yildiz.ce.sesign.repository.SeSignatureRepository;

@Service
public class SeSignatureRepositoryService {
    private final SeSignatureRepository seSignatureRepository;

    public SeSignatureRepositoryService(SeSignatureRepository seSignatureRepository) {
        this.seSignatureRepository = seSignatureRepository;
    }

    public SeSignature saveSignature(SeCertificate cert, byte[] signature) {
        return seSignatureRepository.save(new SeSignature(signature, cert));
    }
}
