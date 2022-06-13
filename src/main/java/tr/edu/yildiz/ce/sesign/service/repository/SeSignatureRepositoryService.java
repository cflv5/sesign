package tr.edu.yildiz.ce.sesign.service.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.exception.SeBaseException;
import tr.edu.yildiz.ce.sesign.domain.entity.SeCertificate;
import tr.edu.yildiz.ce.sesign.domain.entity.SeSignature;
import tr.edu.yildiz.ce.sesign.repository.SeSignatureRepository;
import tr.edu.yildiz.ce.sesign.service.external.FileExternalService;

@Service
public class SeSignatureRepositoryService {
    private final SeSignatureRepository seSignatureRepository;
    private final FileExternalService fileExternalService;

    public SeSignatureRepositoryService(SeSignatureRepository seSignatureRepository,
            FileExternalService fileExternalService) {
        this.seSignatureRepository = seSignatureRepository;
        this.fileExternalService = fileExternalService;
    }

    public SeSignature saveSignature(SeCertificate cert, byte[] signature, String fileId, String tenantId) {
        return seSignatureRepository.save(new SeSignature(signature, cert, fileId, tenantId));
    }

    public SeSignature fetchSignatureById(String id) {
        var sign = seSignatureRepository.findById(id)
                .orElseThrow(() -> new SeBaseException("Signature record not found", HttpStatus.OK));
        fileExternalService.fetchFileWithoutContent(sign.getFileId());
        return sign;
    }
}
