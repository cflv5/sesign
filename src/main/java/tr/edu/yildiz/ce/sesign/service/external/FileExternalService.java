package tr.edu.yildiz.ce.sesign.service.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import tr.edu.yildiz.ce.se.base.exception.SeBaseException;
import tr.edu.yildiz.ce.se.base.service.SeRestService;
import tr.edu.yildiz.ce.sesign.domain.external.FileDto;
import tr.edu.yildiz.ce.sesign.domain.external.response.SimpleFileFetchExternalResponse;

@Service
public class FileExternalService {
    private final SeRestService restService;

    private final String fileApiUrl;

    public FileExternalService(SeRestService restService,
            @Value("${se.endpoints.file-service-api}") String fileApiUrl) {
        this.restService = restService;
        this.fileApiUrl = fileApiUrl;
    }

    public FileDto fetchFileWithoutContent(String fileId) {
        var response = restService.call(fileApiUrl + "/files/" + fileId, null, HttpMethod.GET,
                SimpleFileFetchExternalResponse.class);

        if (!response.getResponseHeader().isSuccess()) {
            throw new SeBaseException("Could not find the file", HttpStatus.OK);
        }
        return response.getFile();
    }
}
