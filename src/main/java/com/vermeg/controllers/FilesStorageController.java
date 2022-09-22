package com.vermeg.controllers;
import java.util.List;
import java.util.stream.Collectors;

import com.vermeg.payload.responses.ApiResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import com.vermeg.payload.responses.FileInfo;
import com.vermeg.service.FilesStorageService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/files")
public class FilesStorageController {

    @Autowired
    FilesStorageService storageService;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ApiOperation(value = "Testing multipart.", notes = "Testing multipart.", consumes="multipart/form-data")
    public ApiResponse<Void> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            String fileName = storageService.save(file);
            message = "Uploaded the file successfully.";
            return new ApiResponse(HttpStatus.OK.value(), message, fileName);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return new ApiResponse<>(HttpStatus.EXPECTATION_FAILED.value(), message, null);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ApiResponse<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
                String url = MvcUriComponentsBuilder
                        .fromMethodName(FilesStorageController.class, "getFile", path.getFileName().toString()).build().toString();
                return new FileInfo(filename, url);
        }).collect(Collectors.toList());
        return new ApiResponse<>(HttpStatus.OK.value(), "", fileInfos);
    }

    @RequestMapping(value = "{filename:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
