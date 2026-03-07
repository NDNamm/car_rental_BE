package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.passwordRequest.ChangePassRequest;
import com.example.car_rental.dto.requestDTO.passwordRequest.ForgotPasswordRequest;

public interface PasswordService {
    void forgotPassword(String email);
    void resetPassword(ForgotPasswordRequest request);
    void changePassword(ChangePassRequest request);
    void checkOtp(ForgotPasswordRequest request);
}
