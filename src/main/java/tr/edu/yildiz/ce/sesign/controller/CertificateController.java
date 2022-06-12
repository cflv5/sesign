package tr.edu.yildiz.ce.sesign.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.edu.yildiz.ce.sesign.domain.request.CertificateInsertionControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.response.CertificateInsertionControllerResponse;
import tr.edu.yildiz.ce.sesign.service.CertificateControllerService;

@RestController
@RequestMapping("/v1/api/certificates")
public class CertificateController {
    private final CertificateControllerService certificateControllerService;

    public CertificateController(CertificateControllerService certificateControllerService) {
        this.certificateControllerService = certificateControllerService;
    }

    @PostMapping(value = { "/", "" })
    public ResponseEntity<CertificateInsertionControllerResponse> generateCertificate(
            @RequestBody CertificateInsertionControllerRequest request) {
        return ResponseEntity.ok().body(certificateControllerService.createSignedCertificate(request));
    }

}
