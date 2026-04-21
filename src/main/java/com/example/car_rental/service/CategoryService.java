package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.CategoryRequest;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {
    void addCategory(CategoryRequest request);
    void updateCategory(Long categoryId, CategoryRequest request);
    void deleteCategory(Long categoryId);
    Page<CategoryResponse> getAllCategories(String name, int page, int size);
}
