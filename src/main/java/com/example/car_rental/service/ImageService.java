package com.example.car_rental.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImageService {
    Map<String, String> uploadImages(MultipartFile image, String folder, String name) throws IOException;
    void deleteImages(String publicId) throws IOException;
}
