package com.boardproject.File;

import lombok.Getter;
import org.springframework.core.io.Resource;

public class FileResponse {

    @Getter
    public static class loadDTO {
        Resource resource;
        String contentType;

        public loadDTO(Resource resource, String contentType) {
            this.resource = resource;
            this.contentType = contentType;
        }
    }
}
