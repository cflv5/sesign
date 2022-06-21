package tr.edu.yildiz.ce.sesign.domain.constants;

public final class CertificateConstants {
    private CertificateConstants() {
        throw new AssertionError("No instance allowed");
    }

    public static final String BC_PROVIDER = "BC";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String CIPHER_SIGNATURE_ALGORITHM = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING";

}
