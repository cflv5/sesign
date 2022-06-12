package tr.edu.yildiz.ce.sesign.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.transaction.Transactional;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.context.TenantContext;
import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.se.base.domain.io.NamedResource;
import tr.edu.yildiz.ce.se.base.exception.SeBaseException;
import tr.edu.yildiz.ce.sesign.domain.request.CertificateInsertionControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.response.CertificateInsertionControllerResponse;
import tr.edu.yildiz.ce.sesign.domain.response.FindTenantsCertificateControllerResponse;
import tr.edu.yildiz.ce.sesign.service.external.UserExternalService;
import tr.edu.yildiz.ce.sesign.service.repository.SeCertificateRepositoryService;
import tr.edu.yildiz.ce.sesign.util.CertificateUtil;

@Service
public class CertificateControllerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateControllerService.class);
    private final SeCertificateRepositoryService seCertificateRepositoryService;
    private final UserExternalService userExternalService;

    public CertificateControllerService(SeCertificateRepositoryService seCertificateRepositoryService,
            UserExternalService userExternalService) {
        this.seCertificateRepositoryService = seCertificateRepositoryService;
        this.userExternalService = userExternalService;
    }

    public CertificateInsertionControllerResponse createSignedCertificate(
            CertificateInsertionControllerRequest request) {
        var tenantId = TenantContext.getCurrentTenant().getTenantId();

        try {
            var keyPair = CertificateUtil.generateKeyPair();
            var tenant = userExternalService.fetchTenantUser(tenantId);
            var signedCertificate = seCertificateRepositoryService.createAndSignCertificate(keyPair, tenant);
            var seCertificate = seCertificateRepositoryService.saveSeCertificate(keyPair.getPrivate(),
                    signedCertificate,
                    request, tenantId);

            return new CertificateInsertionControllerResponse(ResponseHeader.success(), seCertificate.getId());
        } catch (UnrecoverableKeyException | InvalidKeyException | OperatorCreationException
                | SignatureException | CertificateException | KeyStoreException | NoSuchProviderException
                | NoSuchAlgorithmException | IOException e) {
            LOGGER.error("Error occured while creating certificate", e);
            throw new SeBaseException("Could not create certificate", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public FindTenantsCertificateControllerResponse findTenantsCerts() {
        return new FindTenantsCertificateControllerResponse(ResponseHeader.success(),
                seCertificateRepositoryService.findTenantsCertificates());
    }

    @Transactional
    public NamedResource fetchCertificate(String id) {
        var seCert = seCertificateRepositoryService.findCertificateWithId(id);
        return new NamedResource(Base64.decode(seCert.getCert()), seCert.getName() + ".crt");
    }

}
