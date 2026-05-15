package com.example.car_rental.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.car_rental.entity.*;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.CarHandoverRepository;
import com.example.car_rental.repository.CarImagesRepository;
import com.example.car_rental.repository.HandoverImagesRepository;
import com.example.car_rental.service.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;
    private final CarImagesRepository imagesRepository;
    private final HandoverImagesRepository handoverImagesRepository;

    @Override
    @Transactional
    public Map<String, String> uploadImages(MultipartFile image, String folder, String name, String nameImage) throws IOException {
        try {
            return cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "folder", "car_rental/" + folder + "/" + name,
                    "public_id", nameImage,
                    "overwrite", true
            ));
        } catch (Exception e) {
            throw new IOException("Upload file failed: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteImages(String publicId) throws IOException {
        if (publicId == null || publicId.isBlank()) return;

        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        String status = (String) result.get("result");

        if (!"ok".equals(status) && !"not found".equals(status)) {
            throw new IOException("Delete failed: " + result);
        }
    }

    @Override
    @Transactional
    public List<CarImages> uploadCarImages(MultipartFile[] files, String folder, String name, Cars car) {
        List<CarImages> list = new ArrayList<>();

        String safeName = sanitize(name);

        for (MultipartFile fileItem : files) {
            try {
                Map<?, ?> results = cloudinary.uploader().upload(
                        fileItem.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "car_rental/" + folder + "/" + safeName,
                                "overwrite", true
                        )
                );

                String url = results.get("secure_url").toString();
                String publicId = results.get("public_id").toString();

                CarImages images = new CarImages();
                images.setImageUrl(url);
                images.setPublicId(publicId);
                images.setCar(car);

                imagesRepository.save(images);
                list.add(images);

            } catch (IOException e) {
                throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
            }
        }
        return list;
    }

    @Override
    public List<HandoverImages> uploadHandoverImages(MultipartFile[] file, String folder, String bookingCode, String name, CarHandovers carHandovers) {
        List<HandoverImages> list = new ArrayList<>();

        String safeName = sanitize(name);

        for (MultipartFile fileItem : file) {
            try {
                Map<?, ?> results = cloudinary.uploader().upload(
                        fileItem.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "car_rental/" + folder + "/" + bookingCode + "/" + safeName,
                                "overwrite", true
                        )
                );

                String url = results.get("secure_url").toString();
                String publicId = results.get("public_id").toString();

                HandoverImages images = new HandoverImages();;
                images.setImageUrl(url);
                images.setPublicId(publicId);
                images.setCarHandovers(carHandovers);

                handoverImagesRepository.save(images);
                list.add(images);

            } catch (IOException e) {
                throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
            }
        }
        return list;
    }

    private String sanitize(String name) {
        return name
                .trim()
                .toLowerCase()
                .replaceAll("\\s+", "_")
                .replaceAll("[^a-z0-9_]", "");
    }
}
