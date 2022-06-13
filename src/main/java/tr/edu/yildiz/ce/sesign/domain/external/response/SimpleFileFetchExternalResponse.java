package tr.edu.yildiz.ce.sesign.domain.external.response;

import tr.edu.yildiz.ce.se.base.domain.ResponseHeader;
import tr.edu.yildiz.ce.sesign.domain.external.FileDto;

public class SimpleFileFetchExternalResponse {
    private ResponseHeader responseHeader;
    private FileDto file;

    public SimpleFileFetchExternalResponse() {
        super();
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public FileDto getFile() {
        return file;
    }

    public void setFile(FileDto file) {
        this.file = file;
    }

}
