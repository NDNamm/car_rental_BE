package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.passwordRequest.ChangePassRequest;
import com.example.car_rental.dto.requestDTO.passwordRequest.ForgotPasswordRequest;
import com.example.car_rental.entity.Users;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.PasswordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    private static final long OTP_EXPIRE_MINUTES = 5;

    @Override
    public void forgotPassword(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        String otpCode = String.format("%06d", new Random().nextInt(999999));

        String key = "password:otp:" + email;
        redisTemplate.opsForValue().set(key, otpCode, OTP_EXPIRE_MINUTES, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code for Password Reset");
        message.setText("Hello " + user.getFullName() + ",\n\nYour OTP code is: " + otpCode +
                "\nThis code will expire in " + OTP_EXPIRE_MINUTES + " minutes.");
        mailSender.send(message);
    }

    @Override
    @Transactional
    public void resetPassword(ForgotPasswordRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePassRequest request) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_OLD_NOT_MATCH);
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void checkOtp(ForgotPasswordRequest request) {
        String key = "password:otp:" + request.getEmail();
        Object codeInRedis = redisTemplate.opsForValue().get(key);

        if (codeInRedis == null) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        if (!codeInRedis.equals(request.getOtp())) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }
        redisTemplate.delete(key);
    }
}
