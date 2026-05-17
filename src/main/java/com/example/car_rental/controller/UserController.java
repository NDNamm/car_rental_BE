package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.UserRequest.UpdateRoleRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UpdateUserRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.dto.requestDTO.authRequest.RegisterRequest;
import com.example.car_rental.dto.requestDTO.passwordRequest.ChangePassRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.AuthResponse;
import com.example.car_rental.dto.responseDTO.UserResponse;
import com.example.car_rental.service.PasswordService;
import com.example.car_rental.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordService passwordService;

    //ADMIN
    @GetMapping("")
    public ApiResponse<Page<UserResponse>> getAllUser(@RequestParam(required = false) String search,
                                                      @RequestParam(required = false) String role,
                                                      @RequestParam(required = false) Boolean isActive,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {

        Page<UserResponse> users = userService.getAllUsers(search, role, isActive, page, size);

        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("Get user successful")
                .data(users)
                .build();

    }

    @PatchMapping("/{userId}/role")
    public ApiResponse<Void> updateRole(@PathVariable("userId") Long userId,
                                        @RequestBody UpdateRoleRequest request) {
        userService.updateRole(userId, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update role user successful")
                .build();
    }

    @PostMapping("")
    public ApiResponse<UserResponse> register(@RequestBody @Valid UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Add user successful")
                .data(userService.add(request))
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<Void> updateUser(@PathVariable(value = "userId") Long userId,
                                         @RequestBody UpdateUserRequest request) {

        userService.updateUser(userId, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("User updated successfully")
                .build();
    }

    //CLIENT
    @GetMapping("/me")
    public ApiResponse<UserResponse> getProfile() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Get profile successful")
                .data(userService.getProfile(email))
                .build();
    }

    @PutMapping("/me")
    public ApiResponse<Void> editProfile(@RequestBody UserRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.editProfile(email, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Profile updated successfully")
                .build();
    }

    @PatchMapping("/me/avatar")
    public ApiResponse<Void> editAvatar(@RequestPart(value = "avatar") MultipartFile avatar) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.editAvatar(email, avatar);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Avatar updated successfully")
                .build();
    }

    @PatchMapping("/me/change-password")
    public ApiResponse<Void> changePass(@RequestBody ChangePassRequest request) {

        passwordService.changePassword(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Change password successful")
                .build();
    }

}
