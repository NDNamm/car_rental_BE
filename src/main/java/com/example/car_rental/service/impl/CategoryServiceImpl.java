package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.CategoryRequest;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.entity.Categories;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.CategoryRepository;
import com.example.car_rental.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void addCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        Categories categories = Categories.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        categoryRepository.save(categories);
    }

    @Override
    public void updateCategory(Long categoryId, CategoryRequest request) {
        Categories category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        if (request.getName() != null && !request.getName().isEmpty()) {
            if (categoryRepository.existsByNameAndIdNot(request.getName(), categoryId)) {
                throw new AppException(ErrorCode.CATEGORY_EXISTED);
            }
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Categories category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

    @Override
    public Page<CategoryResponse> getAllCategories(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Categories> categoriesPage;

        if (name == null || name.isBlank()) {
            categoriesPage = categoryRepository.findAll(pageable);
        }
        else {
            categoriesPage = categoryRepository.findByName(name, pageable);
        }

        return categoriesPage.map(categories -> CategoryResponse.builder()
                .id(categories.getId())
                .name(categories.getName())
                .description(categories.getDescription())
                .build());

    }
}
