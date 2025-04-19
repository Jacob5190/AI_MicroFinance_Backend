package com.scu.aicontractsummarizerdemo.service;

import com.scu.aicontractsummarizerdemo.entity.LoanDocument;
import com.scu.aicontractsummarizerdemo.repository.LoanDocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final LoanDocumentRepository loanDocumentRepository;

    public FileStorageService(LoanDocumentRepository loanDocumentRepository) {
        this.loanDocumentRepository = loanDocumentRepository;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String store(MultipartFile file) {
        try {
            Files.createDirectories(Paths.get(uploadDir));

            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalFilename;
            Path targetPath = Paths.get(uploadDir).resolve(fileName).normalize();

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Resource load(long fileId) {
        LoanDocument loanDocument =  loanDocumentRepository.findById(fileId).orElseThrow(() -> new RuntimeException("File not found"));
        try {
            String urlPath = loanDocument.getFileUrl();
            Path filePath = Paths.get(urlPath).normalize();
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load file", e);
        }
    }

    public LoanDocument delete(long fileId) throws IOException {
        LoanDocument loanDocument =  loanDocumentRepository.findById(fileId).orElseThrow(() -> new RuntimeException("File not found"));

        String urlPath = loanDocument.getFileUrl();
        Path filePath = Paths.get(urlPath).normalize();
        loanDocumentRepository.deleteById(fileId);
        if (Files.deleteIfExists(filePath)) {
            return loanDocument;
        } else {
            return null;
        }
    }
}
