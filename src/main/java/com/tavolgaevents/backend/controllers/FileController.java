package com.tavolgaevents.backend.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tavolgaevents.backend.payload.request.DeleteFileRequest;
import com.tavolgaevents.backend.payload.request.DownloadFileRequest;
import com.tavolgaevents.backend.models.File;
import com.tavolgaevents.backend.models.User;
import com.tavolgaevents.backend.models.Views;
import com.tavolgaevents.backend.repository.FileRepository;
import com.tavolgaevents.backend.services.FileService;
import com.tavolgaevents.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    UserService userService;

    @Autowired
    FileRepository fileRepository;

    @JsonView(Views.Public.class)
    @PostMapping("/load/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') ")
    public ResponseEntity<Void> uploadFiles(@RequestBody MultipartFile file, @PathVariable String userId) {
        User user = userService.getUserById(Long.valueOf(userId));
        File fileModel = fileService.save(file);
        fileModel.setUser(user);
        fileRepository.save(fileModel);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<File>> getListFiles(@PathVariable String userId) {
        List<File> fileInfos = fileRepository.findByUser(userService.getUserById(Long.valueOf(userId)));
        return fileInfos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> deleteFile(@RequestBody DeleteFileRequest request) {
        File fileInfo = fileRepository.findById(Long.valueOf(request.fileId)).get();
        fileService.delete(fileInfo.path);
        return ResponseEntity.ok().build();
    }


    @JsonView(Views.Public.class)
    @PostMapping("/download")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('ASSESSOR') or hasRole('JURY')")
    public ResponseEntity<?> downloadFile(@RequestBody DownloadFileRequest request) {
        Resource file = fileService.load(request.fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
