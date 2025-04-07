package com.scu.aicontractsummarizerdemo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${app.storage.local-path}")
    private String basePath;

    public String store(MultipartFile file, String folder) throws IOException {
        Path folderPath = Paths.get(basePath, folder);
        Files.createDirectories(folderPath);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = folderPath.resolve(fileName);

        file.transferTo(filePath.toFile());

        // Return a simple local-accessible URL or file reference
        return "/uploads/" + folder + "/" + fileName;
    }
}
