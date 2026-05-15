package com.example.car_rental.service;

import com.example.car_rental.entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ImageService {
    Map<String, String> uploadImages(MultipartFile image, String folder, String name, String nameImage) throws IOException;
    void deleteImages(String publicId) throws IOException;
    List<CarImages> uploadCarImages(MultipartFile[] file, String folder, String name, Cars car);
    List<HandoverImages> uploadHandoverImages(MultipartFile[] file, String folder, String bookingCode, String name, CarHandovers carHandovers);;
}
