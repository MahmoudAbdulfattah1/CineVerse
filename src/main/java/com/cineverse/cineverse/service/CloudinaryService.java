package com.cineverse.cineverse.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Uploads an image file to Cloudinary under the specified folder.
     * Validates the image file type before uploading.
     * Generates a unique UUID for the uploaded image.
     *
     * @param file   the MultipartFile image to upload
     * @param folder the Cloudinary folder to upload the image into
     * @return the generated UUID (public ID) of the uploaded image
     * @throws IOException              if an error occurs during file upload
     * @throws IllegalArgumentException if the file is null, empty, or not a supported image type
     */
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
                            "quality", "auto:best",
                            "width", 800,
                            "height", 800,
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

    /**
     * Deletes an image from Cloudinary by its UUID and folder.
     *
     * @param uuid   the UUID (public ID) of the image to delete
     * @param folder the folder in which the image is stored
     * @return true if the deletion was successful; false otherwise
     * @throws IOException if an error occurs during deletion request
     */
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

    /**
     * Checks if the given MultipartFile has a valid image MIME type.
     * Supported types are: jpeg, jpg, png, webp, and heic.
     *
     * @param file the MultipartFile to check
     * @return true if the file is a valid image type; false otherwise
     */
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

    /**
     * Uploads an image represented as a byte array to Cloudinary under the specified folder.
     * Generates a unique UUID for the uploaded image.
     *
     * @param bytes            the image content as a byte array
     * @param originalFilename the original filename of the image (used for metadata, not for storage)
     * @param folder           the Cloudinary folder to upload the image into
     * @return the generated UUID (public ID) of the uploaded image
     * @throws IOException              if an error occurs during file upload
     * @throws IllegalArgumentException if the byte array is null or empty
     */
    public String uploadImageBytes(byte[] bytes, String originalFilename, String folder) throws IOException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("File bytes cannot be null or empty");
        }

        String uniqueFilename = UUID.randomUUID().toString();

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.asMap(
                            "folder", folder,
                            "public_id", uniqueFilename,
                            "resource_type", "image",
                            "format", "jpg",
                            "quality", "auto:best",
                            "width", 800,
                            "height", 800,
                            "crop", "fill",
                            "gravity", "face"
                    )
            );
            return uniqueFilename;
        } catch (IOException e) {
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
        }
    }
}