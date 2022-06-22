package tr.edu.yildiz.ce.sesign.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.stream.Collectors;

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
import tr.edu.yildiz.ce.sesign.domain.constants.CertificateConstants;
import tr.edu.yildiz.ce.sesign.domain.dto.SeSignatureDto;
import tr.edu.yildiz.ce.sesign.domain.request.NewSignatureControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.request.SignatureVerificationControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.response.NewSignatureControllerResponse;
import tr.edu.yildiz.ce.sesign.domain.response.SignatureDetailControllerResponse;
import tr.edu.yildiz.ce.sesign.domain.response.TenantsSignaturesControllerResponse;
import tr.edu.yildiz.ce.sesign.service.external.FileExternalService;
import tr.edu.yildiz.ce.sesign.service.repository.SeCertificateRepositoryService;
import tr.edu.yildiz.ce.sesign.service.repository.SeOperationLogRepositoryService;
import tr.edu.yildiz.ce.sesign.service.repository.SeSignatureRepositoryService;
import tr.edu.yildiz.ce.sesign.util.CertificateUtil;

@Service
public class SignatureControllerService {
    private final SeSignatureRepositoryService seSignatureRepositoryService;
    private final SeCertificateRepositoryService seCertificateRepositoryService;
    private final FileExternalService fileExternalService;
    private final SeOperationLogRepositoryService logRepositoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureControllerService.class);

    public SignatureControllerService(SeSignatureRepositoryService seSignatureRepositoryService,
            SeCertificateRepositoryService seCertificateRepositoryService,
            FileExternalService fileExternalService,
            SeOperationLogRepositoryService logRepositoryService) {
        this.seSignatureRepositoryService = seSignatureRepositoryService;
        this.seCertificateRepositoryService = seCertificateRepositoryService;
        this.fileExternalService = fileExternalService;
        this.logRepositoryService = logRepositoryService;
    }

    @Transactional
    public NewSignatureControllerResponse signFile(NewSignatureControllerRequest request) {
        try {
            var cert = seCertificateRepositoryService.findCertificateWithIdAsTenant(request.getCertificateId());
            logRepositoryService.save(String.format("Certificate found: %s", cert.getName()));
            var pk = seCertificateRepositoryService.findPrivateKey(cert, request.getCertificatePassword());
            var file = fileExternalService.fetchFileWithoutContent(request.getFileId());
            logRepositoryService.save(String.format("File found: %s", file.getName()));

            var tenantId = TenantContext.getCurrentTenant().getTenantId();

            if (!file.getHashValue().equals(request.getFileHash())) {
                throw new SeBaseException("Integrity of the file could not be verified", HttpStatus.OK);
            }

            Cipher cipher = Cipher.getInstance(CertificateConstants.CIPHER_SIGNATURE_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] digitalSignature = cipher.doFinal(file.getHashValue().getBytes());

            logRepositoryService.save(String.format("Cipher created with '%s' algorith",
                    CertificateConstants.CIPHER_SIGNATURE_ALGORITHM));

            var signature = seSignatureRepositoryService
                    .saveSignature(cert, digitalSignature, file.getFileId(), tenantId);

            logRepositoryService.save(String.format("Signature created: %s",
                    new String(digitalSignature, StandardCharsets.UTF_8).substring(0, 10)));

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
        logRepositoryService.save(String.format("File (%s) found: %s", file.getName(),
                file.getHashValue().substring(0, 10)));

        if (!file.getHashValue().equals(request.getFileHash())) {
            logRepositoryService
                    .save(String.format("Stated file hash is not same with calculated one: %s",
                            request.getFileHash().substring(0, 10)));
            throw new SeBaseException("The integrity of the file could not be verified", HttpStatus.OK);
        }
        logRepositoryService
                .save(String.format("Stated file hash is same with calculated one: %s",
                        request.getFileHash().substring(0, 10)));

        var signature = seSignatureRepositoryService.fetchSignatureById(request.getSignatureId());
        var certificate = CertificateUtil.loadCertificate(signature.getCert());

        logRepositoryService.save(String.format("Signature fetched: %s",
                new String(signature.getSignature(), StandardCharsets.UTF_8).substring(0, 10)));
        logRepositoryService.save(String.format("Using certificate: %s", signature.getCert().getName()));

        Cipher cipher = Cipher.getInstance(CertificateConstants.CIPHER_SIGNATURE_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, certificate.getPublicKey());
        byte[] decryptedMessageHash = cipher.doFinal(signature.getSignature());

        logRepositoryService.save(String.format("Decrypted message: %s",
                new String(decryptedMessageHash, StandardCharsets.UTF_8).substring(0, 10)));
        if (!Arrays.equals(request.getFileHash().getBytes(), decryptedMessageHash)) {
            throw new SeBaseException("Verification failed. The file is not signed by the certificate", HttpStatus.OK);
        }

        return OnlyHeaderControllerResponse.success("Verification successful");
    }

    @Transactional
    public TenantsSignaturesControllerResponse fetchTenantsSignatures() {
        return new TenantsSignaturesControllerResponse(ResponseHeader.success(),
                seSignatureRepositoryService.findTenantsSignatures().stream().map(SeSignatureDto::of)
                        .collect(Collectors.toList()));
    }

    public SignatureDetailControllerResponse fetchSignatureDetail(String id) {
        var seSignature = seSignatureRepositoryService.fetchSignatureById(id);
        fileExternalService.fetchFileWithoutContent(seSignature.getFileId());
        return new SignatureDetailControllerResponse(ResponseHeader.success(), SeSignatureDto.of(seSignature));
    }

}
