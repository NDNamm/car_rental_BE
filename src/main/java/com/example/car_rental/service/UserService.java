package com.example.car_rental.service;


import com.example.car_rental.dto.requestDTO.UserRequest;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserRequest> getAllUsers(String email, String fullName, String phoneNumber, int page, int size);

    void updateRole(Long id, UserRequest userRequest);

    void delete(Long id);

    UserRequest editProfile(UserRequest userRequest);
}