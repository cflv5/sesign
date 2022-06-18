package tr.edu.yildiz.ce.sesign.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.edu.yildiz.ce.sesign.domain.request.CertificateInsertionControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.response.CertificateInsertionControllerResponse;
import tr.edu.yildiz.ce.sesign.domain.response.FetchSeCertificateDetailControllerResponse;
import tr.edu.yildiz.ce.sesign.domain.response.FindTenantsCertificateControllerResponse;
import tr.edu.yildiz.ce.sesign.service.CertificateControllerService;

@RestController
@RequestMapping("/v1/api/certificates")
public class CertificateController {
    private final CertificateControllerService certificateControllerService;

    public CertificateController(CertificateControllerService certificateControllerService) {
        this.certificateControllerService = certificateControllerService;
    }

    @PostMapping(value = "/new")
    public ResponseEntity<CertificateInsertionControllerResponse> generateCertificate(
            @RequestBody CertificateInsertionControllerRequest request) {
        return ResponseEntity.ok().body(certificateControllerService.createSignedCertificate(request));
    }

    @GetMapping(value = "/mine")
    public ResponseEntity<FindTenantsCertificateControllerResponse> findTenantsCerts() {
        return ResponseEntity.ok().body(certificateControllerService.findTenantsCerts());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Resource> fetchCertificate(@PathVariable("id") String id) {
        var resource = certificateControllerService.fetchCertificate(id);
        return ResponseEntity
                .ok()
                .contentLength(resource.contentLength())
                .contentType(new MediaType("application", "x-x509-user-cert"))
                .header("Content-Disposition", "attachment; filename=" + resource.getFilename())
                .body(resource);
    }
    
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<FetchSeCertificateDetailControllerResponse> fetchCertificateDetail(@PathVariable("id") String id) {
        return ResponseEntity
                .ok()
                .body(certificateControllerService.fetchCertificateDetail(id));
    }
}
