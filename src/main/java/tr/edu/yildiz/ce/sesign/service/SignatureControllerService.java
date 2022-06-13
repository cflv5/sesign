package tr.edu.yildiz.ce.sesign.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.transaction.Transactional;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.context.TenantContext;
import tr.edu.yildiz.ce.se.base.domain.OnlyHeaderControllerResponse;
import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.se.base.domain.io.NamedResource;
import tr.edu.yildiz.ce.se.base.exception.SeBaseException;
import tr.edu.yildiz.ce.sesign.domain.dto.SeSignatureDto;
import tr.edu.yildiz.ce.sesign.domain.request.NewSignatureControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.request.SignatureVerificationControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.response.NewSignatureControllerResponse;
import tr.edu.yildiz.ce.sesign.service.external.FileExternalService;
import tr.edu.yildiz.ce.sesign.service.repository.SeCertificateRepositoryService;
import tr.edu.yildiz.ce.sesign.service.repository.SeSignatureRepositoryService;
import tr.edu.yildiz.ce.sesign.util.CertificateUtil;

@Service
public class SignatureControllerService {
    private final SeSignatureRepositoryService seSignatureRepositoryService;
    private final SeCertificateRepositoryService seCertificateRepositoryService;
    private final FileExternalService fileExternalService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureControllerService.class);

    public SignatureControllerService(SeSignatureRepositoryService seSignatureRepositoryService,
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

            var tenantId = TenantContext.getCurrentTenant().getTenantId();

            if (!file.getHashValue().equals(request.getFileHash())) {
                throw new SeBaseException("Integrity of the file could not be verified", HttpStatus.OK);
            }

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] digitalSignature = cipher.doFinal(file.getHashValue().getBytes());

            var signature = seSignatureRepositoryService
                    .saveSignature(cert, digitalSignature, file.getFileId(), tenantId);

            return new NewSignatureControllerResponse(ResponseHeader.success(), SeSignatureDto.of(signature));
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException
                | IOException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            final var errorMessage = "An error occured while signing the request";
            LOGGER.error(errorMessage, e);
            throw new SeBaseException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public NamedResource fetchSignatureFile(String id) {
        var seSignature = seSignatureRepositoryService.fetchSignatureById(id);
        return new NamedResource(Base64.encode(seSignature.getSignature()), id);
    }

    @Transactional
    public OnlyHeaderControllerResponse verifySignature(
            SignatureVerificationControllerRequest request) throws CertificateException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        var file = fileExternalService.fetchFileWithoutContent(request.getFileId());

        if (!file.getHashValue().equals(request.getFileHash())) {
            throw new SeBaseException("The integrity of the file could not be verified", HttpStatus.OK);
        }

        var signature = seSignatureRepositoryService.fetchSignatureById(request.getSignatureId());
        var certificate = CertificateUtil.loadCertificate(signature.getCert());

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, certificate.getPublicKey());
        byte[] decryptedMessageHash = cipher.doFinal(signature.getSignature());

        if (!Arrays.equals(request.getFileHash().getBytes(), decryptedMessageHash)) {
            throw new SeBaseException("Verification failed. The file is not signed by the certificate", HttpStatus.OK);
        }

        return OnlyHeaderControllerResponse.success("Verification successful");
    }

}
