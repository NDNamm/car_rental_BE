package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.UserRequest.UpdateRoleRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.dto.requestDTO.passwordRequest.ChangePassRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.UserResponse;
import com.example.car_rental.service.PasswordService;
import com.example.car_rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final PasswordService passwordService;

    //ADMIN
    @GetMapping("")
    public ApiResponse<Page<UserResponse>> getAllUser(@RequestParam(required = false) String email,
                                                     @RequestParam(required = false) String fullName,
                                                     @RequestParam(required = false) String phoneNumber,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size){

        Page<UserResponse> users =
                userService.getAllUsers(email, fullName, phoneNumber, page, size);

        return ApiResponse.<Page<UserResponse>>builder()
                .code(200)
                .message("Get profile successful")
                .data(users)
                .build();

    }

    @PatchMapping("/{id}/role")
    public ApiResponse<Void> updateRole(@PathVariable("user_id") Long userId, @RequestBody UpdateRoleRequest request) {
        userService.updateRole(userId, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update role user successful")
                .build();
    }


    //CLIENT
    @GetMapping("/me")
    public ApiResponse<UserResponse> getProfile(){

        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Get profile successful")
                .data(userService.getProfile())
                .build();
    }

    @PostMapping("/me")
    public ApiResponse<Void> editProfile(@RequestBody UserRequest request)  {

        userService.editProfile(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Profile updated successfully")
                .build();
    }

    @PatchMapping("/me/avatar")
    public ApiResponse<Void> editAvatar(@RequestPart(value = "avatar") MultipartFile avatar) throws IOException {
        userService.editAvatar(avatar);
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
