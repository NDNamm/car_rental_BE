package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.CategoryRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UpdateRoleRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.dto.requestDTO.passwordRequest.ChangePassRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.dto.responseDTO.UserResponse;
import com.example.car_rental.service.CategoryService;
import com.example.car_rental.service.PasswordService;
import com.example.car_rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public ApiResponse<Page<CategoryResponse>> getAllCategory(@RequestParam(defaultValue = "0", required = false) int page,
                                                             @RequestParam(defaultValue = "5") int size,
                                                             @RequestParam(required = false) String name) {
        return ApiResponse.<Page<CategoryResponse>>builder()
                .code(200)
                .data(categoryService.getAllCategories(name, page, size))
                .build();
    }

    @PostMapping(value = "/add")
    public ApiResponse<Void> addCategory(@RequestBody  CategoryRequest request){

        categoryService.addCategory(request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Create category successful")
                .build();
    }


    @PutMapping(value = "/update/{categoryId}")
    public ApiResponse<Void> updateCategory(@PathVariable(value = "categoryId") Long categoryId,
                                            @RequestBody CategoryRequest request){

        categoryService.updateCategory(categoryId, request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update category successful")
                .build();
    }

    @DeleteMapping("/delete/{categoryId}")
    public ApiResponse<Void> deleteCategory( @PathVariable("categoryId") Long categoryId) {

        categoryService.deleteCategory(categoryId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete category successful")
                .build();
    }

}
