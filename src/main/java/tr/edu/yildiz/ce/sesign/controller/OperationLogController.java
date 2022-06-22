package tr.edu.yildiz.ce.sesign.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tr.edu.yildiz.ce.sesign.domain.response.TenantLogsControllerResponse;
import tr.edu.yildiz.ce.sesign.service.OperationLogControllerService;

@RestController
@RequestMapping("/v1/api/logs")
public class OperationLogController {
    private final OperationLogControllerService operationLogControllerService;

    public OperationLogController(OperationLogControllerService operationLogControllerService) {
        this.operationLogControllerService = operationLogControllerService;
    }

    @GetMapping({ "/", "" })
    public ResponseEntity<TenantLogsControllerResponse> getMethodName() {
        return ResponseEntity.ok().body(operationLogControllerService.fetchTenantsLogs());
    }

}
