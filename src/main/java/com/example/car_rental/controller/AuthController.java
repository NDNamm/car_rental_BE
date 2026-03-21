package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.authRequest.LoginRequest;
import com.example.car_rental.dto.requestDTO.authRequest.RegisterRequest;
import com.example.car_rental.dto.requestDTO.passwordRequest.ForgotPasswordRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.AuthResponse;
import com.example.car_rental.service.AuthService;
import com.example.car_rental.service.PasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordService passwordService;

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Login successful")
                .data(authService.login(request, response))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Register successful")
                .data(authService.register(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Logout successful")
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Token refreshed successfully")
                .data(authService.refreshToken(request, response))
                .build();
    }

    @PostMapping("/google-login")
    public ApiResponse<AuthResponse> loginWithGoogle(
            @RequestBody String tokenId,
            HttpServletResponse response
    ) {
        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Google login successful")
                .data(authService.loginWithGoogle(tokenId, response))
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordService.forgotPassword(request.getEmail());
        return ApiResponse.<String>builder()
                .code(200)
                .message("OTP sent to your email")
                .build();
    }

    @PostMapping("/check-otp")
    public ApiResponse<String> checkOtpCode(@RequestBody ForgotPasswordRequest request) {
        passwordService.checkOtp(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("OTP ok")
                .build();
    }

    @PatchMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody ForgotPasswordRequest request) {
        passwordService.resetPassword(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Password changed successfully")
                .build();
    }

}
