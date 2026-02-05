package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.authRequest.LoginRequest;
import com.example.car_rental.dto.requestDTO.authRequest.RegisterRequest;
import com.example.car_rental.dto.responseDTO.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface AuthService {

    AuthResponse login(LoginRequest loginRequest, HttpServletResponse response);

    AuthResponse register(RegisterRequest registerRequest);

    void logout(HttpServletRequest request, HttpServletResponse response);

    AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    AuthResponse loginWithGoogle(String idToken, HttpServletResponse response);
}
