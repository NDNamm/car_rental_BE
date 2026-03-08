package com.example.car_rental.service;


import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import org.springframework.data.domain.Page;

public interface UserService {
    //ADMIN
    Page<UserRequest> getAllUsers(String email, String fullName, String phoneNumber, int page, int size);

    void updateRole(Long id, UserRequest userRequest);

    void delete(Long id);

    //USER
    UserRequest editProfile(UserRequest userRequest);
}