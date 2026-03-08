package com.example.car_rental.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.car_rental.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    @Override
    public Map<String, String> uploadImages(MultipartFile image, String folder, String name) throws IOException {
        try {
            return cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "project", "car_rental",
                    "folder", folder,
                    "name", name
            ));
        } catch (Exception e) {
            throw new IOException("Upload file failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteImages(String publicId) throws IOException {
        if (publicId != null && !publicId.isEmpty()) {
            Map<String, Object> options = new HashMap<>();
            cloudinary.uploader().destroy(publicId, options);
        }
    }
}
