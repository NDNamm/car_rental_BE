package com.example.car_rental.service;


import com.example.car_rental.dto.requestDTO.UserRequest.UpdateRoleRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.dto.responseDTO.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    //ADMIN
    Page<UserResponse> getAllUsers(String email, String fullName, String phoneNumber, int page, int size);

    void updateRole(Long id, UpdateRoleRequest request);

    void delete(Long id);

    //USER
    UserResponse getProfile();
    void editProfile(UserRequest userRequest);
    void editAvatar(MultipartFile avatar) throws IOException;
}