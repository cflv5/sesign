package tr.edu.yildiz.ce.sesign.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;

import tr.edu.yildiz.ce.sesign.domain.constants.CertificateConstants;

public final class CertificateUtil {
    private CertificateUtil() {
        throw new AssertionError("No instance allowed");
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(CertificateConstants.KEY_ALGORITHM,
                CertificateConstants.BC_PROVIDER);
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.genKeyPair();
    }

    public static Date today() {
        return Calendar.getInstance().getTime();
    }

    public static Date certificateExpirationDate(int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    public static KeyStore loadKeyStore(byte[] buff, String password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        InputStream fis = new ByteArrayInputStream(buff);
        ks.load(fis, password.toCharArray());
        return ks;
    }
}
