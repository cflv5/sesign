package tr.edu.yildiz.ce.sesign.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.edu.yildiz.ce.sesign.domain.request.NewSignatureControllerRequest;
import tr.edu.yildiz.ce.sesign.domain.response.NewSignatureControllerResponse;
import tr.edu.yildiz.ce.sesign.service.SignatureControlleService;

@RestController
@RequestMapping("/v1/api/signatures")
public class SignatureController {
    private final SignatureControlleService signatureControlleService;

    public SignatureController(SignatureControlleService signatureControlleService) {
        this.signatureControlleService = signatureControlleService;
    }

    @PostMapping(value = "/new")
    public ResponseEntity<NewSignatureControllerResponse> postMethodName(
            @RequestBody @Valid NewSignatureControllerRequest request) {

        return ResponseEntity.ok().body(signatureControlleService.signFile(request));
    }

}
