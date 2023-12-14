package com.ecommerce.service;

import com.ecommerce.exception.ProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class UploadFileServiceTest {

    private UploadFileService uploadFileService;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        uploadFileService = new UploadFileService();
    }

    @Test
    public void testSaveImage() {
        MultipartFile imageFile = new MockMultipartFile("imageFile", "hello.png", "image/png", "some image".getBytes());

        String result = uploadFileService.saveImage(imageFile);

        assertEquals("hello.png", result);
        Path imagePath = Paths.get(UploadFileService.IMAGE_FOLDER, imageFile.getOriginalFilename());
        assert(Files.exists(imagePath));
    }

    @Test
    public void testSaveImage_EmptyFile() {
        MultipartFile imageFile = new MockMultipartFile("imageFile", "", "image/png", "".getBytes());

        String result = uploadFileService.saveImage(imageFile);

        assertEquals(UploadFileService.IMAGE_BY_DEFAULT, result);
        Path imagePath = Paths.get(UploadFileService.IMAGE_FOLDER, UploadFileService.IMAGE_BY_DEFAULT);
        assert(Files.exists(imagePath));
    }

    @Test
    public void testSaveImage_NullFile() {
        String result = uploadFileService.saveImage(null);

        assertEquals(UploadFileService.IMAGE_BY_DEFAULT, result);
        Path imagePath = Paths.get(UploadFileService.IMAGE_FOLDER, UploadFileService.IMAGE_BY_DEFAULT);
        assert(Files.exists(imagePath));
    }

    @Test
    public void testDeleteImage() {
        String imageName = "test.png";
        Path imagePath = Paths.get(UploadFileService.IMAGE_FOLDER, imageName);

        assertDoesNotThrow(() -> Files.write(imagePath, new byte[0]));

        assertTrue(Files.exists(imagePath));

        assertDoesNotThrow(() -> uploadFileService.deleteImage(imageName));

        assertTrue(Files.notExists(imagePath));
    }
}
