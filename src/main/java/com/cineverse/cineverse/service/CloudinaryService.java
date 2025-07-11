package com.cineverse.cineverse.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        if (!isValidImageFile(file)) {
            throw new IllegalArgumentException("File must be an image (jpg, jpeg, png, webp)");
        }

        String uniqueFilename = UUID.randomUUID().toString();

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "public_id", uniqueFilename,
                            "resource_type", "image",
                            "format", "jpg",
                            "quality", "auto:good",
                            "width", 400,
                            "height", 400,
                            "crop", "fill",
                            "gravity", "face"
                    )
            );

            String publicId = (String) uploadResult.get("public_id");
            return uniqueFilename;

        } catch (IOException e) {
            throw new IOException("Failed to upload image: " + e.getMessage());
        }
    }


    public boolean deleteImage(String uuid, String folder) throws IOException {
        try {
            String publicId = folder + "/" + uuid;

            Map<String, Object> deleteResult = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "image")
            );

            String result = (String) deleteResult.get("result");
            return "ok".equals(result);

        } catch (IOException e) {
            return false;
        }
    }

    private boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/jpg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/webp") ||
                        contentType.equals("image/heic")
        );
    }
}