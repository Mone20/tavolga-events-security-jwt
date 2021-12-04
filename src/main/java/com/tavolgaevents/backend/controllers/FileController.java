package com.tavolgaevents.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tavolgaevents.backend.models.Views;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/files")
public class FileController {

    @JsonView(Views.Public.class)
    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addFiles(@RequestBody List<MultipartFile> files, @RequestParam String userId) {
        return ResponseEntity.ok().build();
    }
}
