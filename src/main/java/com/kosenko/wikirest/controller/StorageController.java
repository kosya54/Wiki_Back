package com.kosenko.wikirest.controller;

import com.kosenko.wikirest.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> upload(@RequestParam(name = "files") List<MultipartFile> multipartFiles) {
        return new ResponseEntity<>(storageService.upload(multipartFiles), HttpStatus.OK);
    }
}
