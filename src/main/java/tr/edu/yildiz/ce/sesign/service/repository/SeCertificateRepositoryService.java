package tr.edu.yildiz.ce.sesign.service.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.context.TenantContext;
import tr.edu.yildiz.ce.se.base.domain.common.TenantUser;
import tr.edu.yildiz.ce.se.base.exception.SeBaseException;
import tr.edu.yildiz.ce.sesign.domain.constants.CertificateConstants;
import tr.edu.yildiz.ce.sesign.domain.dto.SeCertificateDto;
import tr.edu.yildiz.ce.sesign.domain.entity.SeCertificate;
import tr.edu.yildiz.ce.sesign.domain.entity.SeCertificateStatus;
import tr.edu.yildiz.ce.sesign.domain.request.CertificateInsertionControllerRequest;
import tr.edu.yildiz.ce.sesign.repository.SeCertificateRepository;
import tr.edu.yildiz.ce.sesign.util.CertificateUtil;

@Service
public class SeCertificateRepositoryService {
        private final SeCertificateRepository seCertificateRepository;
        private final KeyStore rootCA;

        private final String keyStorePassword;

        public SeCertificateRepositoryService(SeCertificateRepository seCertificateRepository,
                        @Value("${se.key-store.password}") String keyStorePassword)
                        throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
                this.seCertificateRepository = seCertificateRepository;
                this.keyStorePassword = keyStorePassword;
                this.rootCA = loadRootCA();
        }

        private KeyStore loadRootCA()
                        throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
                KeyStore ks = KeyStore.getInstance("PKCS12");
                InputStream fis = new ClassPathResource("ytucese.p12").getInputStream();
                ks.load(fis, keyStorePassword.toCharArray());
                fis.close();
                return ks;
        }

        public X509Certificate createAndSignCertificate(KeyPair keyPair, TenantUser user)
                        throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
                        OperatorCreationException, CertIOException, CertificateException, InvalidKeyException,
                        NoSuchProviderException, SignatureException {
                var rootPK = (PrivateKey) rootCA.getKey("1", keyStorePassword.toCharArray());
                var rootCrt = (X509Certificate) rootCA.getCertificate("1");

                var startDate = CertificateUtil.today();
                var endDate = CertificateUtil.certificateExpirationDate(2);

                X500Name issuedCertSubject = new X500Name(
                                String.format("C=TR, CN=%s, O=Yildiz Technical University", user.formalName()));
                BigInteger issuedCertSerialNum = new BigInteger(Long.toString(new SecureRandom().nextLong()));

                // CSR Builder
                PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                                issuedCertSubject,
                                keyPair.getPublic());
                JcaContentSignerBuilder csrBuilder = new JcaContentSignerBuilder(
                                CertificateConstants.SIGNATURE_ALGORITHM)
                                .setProvider(CertificateConstants.BC_PROVIDER);

                // CSRs
                ContentSigner csrContentSigner = csrBuilder.build(rootPK);
                PKCS10CertificationRequest csr = p10Builder.build(csrContentSigner);

                X509v3CertificateBuilder issuedCertBuilder = new X509v3CertificateBuilder(
                                X500Name.getInstance(rootCrt.getSubjectX500Principal().getEncoded()),
                                issuedCertSerialNum,
                                startDate, endDate, csr.getSubject(), csr.getSubjectPublicKeyInfo());

                // Add Extensions
                JcaX509ExtensionUtils issuedCertExtUtils = new JcaX509ExtensionUtils();
                issuedCertBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
                issuedCertBuilder.addExtension(Extension.authorityKeyIdentifier, false,
                                issuedCertExtUtils.createAuthorityKeyIdentifier(rootCrt));
                issuedCertBuilder.addExtension(Extension.subjectKeyIdentifier, false,
                                issuedCertExtUtils.createSubjectKeyIdentifier(csr.getSubjectPublicKeyInfo()));

                issuedCertBuilder.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.digitalSignature));

                // Signing certificate
                X509CertificateHolder issuedCertHolder = issuedCertBuilder.build(csrContentSigner);
                X509Certificate issuedCert = new JcaX509CertificateConverter()
                                .setProvider(CertificateConstants.BC_PROVIDER)
                                .getCertificate(issuedCertHolder);

                issuedCert.verify(rootCrt.getPublicKey(), CertificateConstants.BC_PROVIDER);

                return issuedCert;
        }

        public SeCertificate saveSeCertificate(PrivateKey pk, X509Certificate cert,
                        CertificateInsertionControllerRequest request, String tenantId)
                        throws NoSuchAlgorithmException, CertificateException, KeyStoreException,
                        NoSuchProviderException, IOException {
                var seCert = new SeCertificate();

                seCert.setTenantId(tenantId);
                seCert.setCert(Base64.encode(cert.getEncoded()));
                seCert.setStatus(SeCertificateStatus.ACTIVE);
                seCert.setName(request.getName());
                seCert.setKeyStore(Base64.encode(createKeyStoreWith(pk, cert, request.getPassword())));

                return seCertificateRepository.save(seCert);
        }

        private byte[] createKeyStoreWith(PrivateKey pk, X509Certificate cert, String password)
                        throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException,
                        NoSuchProviderException {

                KeyStore sslKeyStore = KeyStore.getInstance("PKCS12", CertificateConstants.BC_PROVIDER);
                sslKeyStore.load(null, null);
                sslKeyStore.setKeyEntry("1", pk, password.toCharArray(), new Certificate[] { cert });
                var keyStoreOs = new ByteArrayOutputStream();
                sslKeyStore.store(keyStoreOs, password.toCharArray());
                return keyStoreOs.toByteArray();
        }

        @Transactional
        public List<SeCertificateDto> findTenantsCertificates() {
                var tenantId = TenantContext.getCurrentTenant().getTenantId();
                return seCertificateRepository.findByTenantId(tenantId).stream()
                                .map(SeCertificateDto::of).collect(Collectors.toList());
        }

        public SeCertificate findCertificateWithId(String id) {
                return seCertificateRepository.findById(id)
                                .orElseThrow(() -> new SeBaseException("Could not found certificate",
                                                HttpStatus.NOT_FOUND));
        }

        public PrivateKey findPrivateKey(SeCertificate seCert, String password)
                        throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
                        UnrecoverableKeyException {
                var keyStore = CertificateUtil.loadKeyStore(Base64.decode(seCert.getKeyStore()), password);
                return (PrivateKey) keyStore.getKey("1", password.toCharArray());
        }

        public SeCertificate findCertificateWithIdAsTenant(String certificateId) {
                var cert = seCertificateRepository.findActiveById(certificateId).orElseThrow(
                                () -> new SeBaseException("Could not found the certificate", HttpStatus.NOT_FOUND));

                if (!cert.getTenantId().equals(TenantContext.getCurrentTenant().getTenantId())) {
                        throw new SeBaseException("Unauthorized", HttpStatus.UNAUTHORIZED);
                }

                return cert;
        }

        public boolean validateCertificate(SeCertificate certificate) throws InvalidKeyException, CertificateException,
                        NoSuchAlgorithmException, NoSuchProviderException, SignatureException, KeyStoreException {
                CertificateUtil.loadCertificate(certificate).verify(rootCA.getCertificate("1").getPublicKey());
                return true;
        }

        public X509Certificate fetchRootCertificate() throws KeyStoreException {
                return (X509Certificate) rootCA.getCertificate("1");
        }
}
