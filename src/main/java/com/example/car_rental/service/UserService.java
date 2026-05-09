package com.example.car_rental.service;


import com.example.car_rental.dto.requestDTO.UserRequest.UpdateRoleRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UpdateUserRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.dto.responseDTO.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    //ADMIN
    Page<UserResponse> getAllUsers(String search ,String role, Boolean isActive, int page, int size);
    void updateRole(Long id, UpdateRoleRequest request);
    void updateUser(Long id, UpdateUserRequest request);
    UserResponse add(UserRequest request);

    //USER
    UserResponse getProfile(String email);
    void editProfile(String email, UserRequest userRequest);
    void editAvatar(String email, MultipartFile avatar) throws IOException;
}