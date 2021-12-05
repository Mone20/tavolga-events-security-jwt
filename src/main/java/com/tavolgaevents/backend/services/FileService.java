package com.tavolgaevents.backend.services;

import java.nio.file.Path;
import java.util.stream.Stream;

import com.tavolgaevents.backend.models.File;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public void init();

    void delete(String fileName);

    public File save(MultipartFile file);

    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();
}
