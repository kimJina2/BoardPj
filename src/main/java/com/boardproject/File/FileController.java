package com.boardproject.File;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/file")
@RequiredArgsConstructor
@RestController
public class FileController {
    private final FileService fileService;

    @PostMapping(value = "/uploadSummernoteImageFile", produces = "application/json")
    public JsonObject uploadSummernoteImageFile(@RequestParam MultipartFile file) {
        return fileService.save(file);
    }

    @GetMapping("/load")
    public ResponseEntity<?> load(@RequestParam String path, @RequestParam String name) {
        FileResponse.loadDTO loadDTO = fileService.load(path, name);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, loadDTO.getContentType());

        Resource resource = loadDTO.getResource();

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
