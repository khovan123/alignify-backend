package com.api.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class FileStorageService {

    @Autowired
    private Cloudinary cloudinary;
    @Value("${cloudinary.upload-preset}")
    private String uploadPreset;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_FILE_SIZE;
    private final String ALLOWED_EXTENSIONS[] = {".png", ".jpg", ".jpeg"};
    private long parsedMaxFileSize;

    @Autowired
    public void setMaxFileSize(@Value("${spring.servlet.multipart.max-file-size}") String MAX_FILE_SIZE) {
        this.MAX_FILE_SIZE = MAX_FILE_SIZE;
        this.parsedMaxFileSize = parseFileSize(MAX_FILE_SIZE);
    }

    @SuppressWarnings("rawtypes")
    public String storeFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new Exception("File is empty");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !hasValidExtension(originalFilename)) {
            throw new Exception("Only PNG, JPG, JPEG files are allowed");
        }
        if (file.getSize() > parsedMaxFileSize) {
            throw new Exception("File size exceeds " + MAX_FILE_SIZE + " limit");
        }
        String uuid  = UUID.randomUUID().toString();
        String filename = originalFilename.replaceAll("\\s+", "_");
        String publicId = uuid + "_" + filename;
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "upload_preset", uploadPreset,
                            "use_filename", false,
                            "unique_filename", false,
                            "resource_type", "auto",
                            "public_id", publicId
                            )
            );
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new Exception("Failed to upload file: " + e.getMessage());
        }
    }

    private boolean hasValidExtension(String filename) {
        return Arrays.stream(ALLOWED_EXTENSIONS)
                .anyMatch(ext -> filename.toLowerCase().endsWith(ext));
    }

    private long parseFileSize(String size) {
        try {
            size = size.toUpperCase();
            if (size.endsWith("MB")) {
                return Long.parseLong(size.replace("MB", "").trim()) * 1024 * 1024;
            } else if (size.endsWith("KB")) {
                return Long.parseLong(size.replace("KB", "").trim()) * 1024;
            } else {
                return Long.parseLong(size);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid max-file-size configuration: " + size);
        }
    }
}