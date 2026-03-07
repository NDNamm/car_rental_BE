package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.UserRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ApiResponse<Page<UserRequest>> getAllUser(@RequestParam(required = false) String email,
                                                     @RequestParam(required = false) String fullName,
                                                     @RequestParam(required = false) String phoneNumber,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size){

        ApiResponse<Page<UserRequest>> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getAllUsers(email,fullName,phoneNumber,page, size));
        return apiResponse;

    }


}
