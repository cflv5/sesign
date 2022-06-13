package tr.edu.yildiz.ce.sesign.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.se.base.exception.SeBaseException;
import tr.edu.yildiz.ce.sesign.domain.dto.SeSignatureDto;
import tr.edu.yildiz.ce.sesign.domain.request.NewSignatureControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.response.NewSignatureControllerResponse;
import tr.edu.yildiz.ce.sesign.service.external.FileExternalService;
import tr.edu.yildiz.ce.sesign.service.repository.SeCertificateRepositoryService;
import tr.edu.yildiz.ce.sesign.service.repository.SeSignatureRepositoryService;

@Service
public class SignatureControlleService {
    private final SeSignatureRepositoryService seSignatureRepositoryService;
    private final SeCertificateRepositoryService seCertificateRepositoryService;
    private final FileExternalService fileExternalService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureControlleService.class);

    public SignatureControlleService(SeSignatureRepositoryService seSignatureRepositoryService,
            SeCertificateRepositoryService seCertificateRepositoryService,
            FileExternalService fileExternalService) {
        this.seSignatureRepositoryService = seSignatureRepositoryService;
        this.seCertificateRepositoryService = seCertificateRepositoryService;
        this.fileExternalService = fileExternalService;
    }

    public NewSignatureControllerResponse signFile(NewSignatureControllerRequest request) {
        try {
            var cert = seCertificateRepositoryService.findCertificateWithIdAsTenant(request.getCertificateId());
            var pk = seCertificateRepositoryService.findPrivateKey(cert, request.getCertificatePassword());
            var file = fileExternalService.fetchFileWithoutContent(request.getFileId());

            if (!file.getHashValue().equals(request.getFileHash())) {
                throw new SeBaseException("Integrity of the file could not be verified", HttpStatus.OK);
            }

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] digitalSignature = cipher.doFinal(file.getHashValue().getBytes());

            var signature = seSignatureRepositoryService.saveSignature(cert, digitalSignature);

            return new NewSignatureControllerResponse(ResponseHeader.success(), SeSignatureDto.of(signature));
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException
                | IOException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            final var errorMessage = "An error occured while signing the request";
            LOGGER.error(errorMessage, e);
            throw new SeBaseException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
