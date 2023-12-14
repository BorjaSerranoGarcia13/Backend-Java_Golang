package com.ecommerce.service;

import com.ecommerce.exception.ProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileService.class);
    public static final String IMAGE_FOLDER = "images/";
    public static final String IMAGE_BY_DEFAULT = "default.jpg";

    public String saveImage(MultipartFile newImageFile) {
        try {
            if (newImageFile != null && !newImageFile.isEmpty()) {
                Path imagePath = Paths.get(IMAGE_FOLDER, newImageFile.getOriginalFilename());
                byte[] imageBytes = newImageFile.getBytes();
                Files.write(imagePath, imageBytes);

                logger.info("Image saved at: {}", imagePath);
                return newImageFile.getOriginalFilename();
            } else {
                logger.info("ImageFile is empty. Using default image.");

                Path defaultImagePath = Paths.get(IMAGE_FOLDER, IMAGE_BY_DEFAULT);

                if (!Files.exists(defaultImagePath)) {
                    Files.write(defaultImagePath, new byte[0]);
                }

                return IMAGE_BY_DEFAULT;
            }
        } catch (IOException e) {
            throw new ProductException("Error saving image");
        }
    }

    public void deleteImage(String imageName) {
        try {
            if (!imageName.equals(IMAGE_BY_DEFAULT)) {
                Path imagePath = Paths.get(IMAGE_FOLDER, imageName);
                Files.deleteIfExists(imagePath);
            }

        } catch (IOException e) {
            throw new ProductException("Error deleting the image.", e);
        }
    }
}
