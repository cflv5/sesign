package tr.edu.yildiz.ce.sesign.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.edu.yildiz.ce.se.base.domain.OnlyHeaderControllerResponse;
import tr.edu.yildiz.ce.sesign.domain.request.NewSignatureControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.request.SignatureVerificationControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.response.NewSignatureControllerResponse;
import tr.edu.yildiz.ce.sesign.domain.response.TenantsSignaturesControllerResponse;
import tr.edu.yildiz.ce.sesign.service.SignatureControllerService;

@RestController
@RequestMapping("/v1/api/signatures")
public class SignatureController {
    private final SignatureControllerService signatureControlleService;

    public SignatureController(SignatureControllerService signatureControlleService) {
        this.signatureControlleService = signatureControlleService;
    }

    @PostMapping(value = "/new")
    public ResponseEntity<NewSignatureControllerResponse> signFile(
            @RequestBody @Valid NewSignatureControllerRequest request) {
        return ResponseEntity.ok().body(signatureControlleService.signFile(request));
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> fetchSignatureFile(@PathVariable("id") String id) {
        var signature = signatureControlleService.fetchSignatureFile(id);
        return ResponseEntity.ok()
                .contentLength(signature.contentLength())
                .header("Content-Disposition", "attachment; filename=" + signature.getFilename())
                .contentType(MediaType.TEXT_PLAIN)
                .body(signature);
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<OnlyHeaderControllerResponse> verifySignatureInternal(
            @RequestBody @Valid SignatureVerificationControllerRequest request)
            throws InvalidKeyException, CertificateException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, IOException {
        return ResponseEntity.ok().body(signatureControlleService.verifySignature(request));
    }
    
    @GetMapping(value = "/mine")
    public ResponseEntity<TenantsSignaturesControllerResponse> fetchTenantsSignatures(){
        return ResponseEntity.ok().body(signatureControlleService.fetchTenantsSignatures());
    }

}
