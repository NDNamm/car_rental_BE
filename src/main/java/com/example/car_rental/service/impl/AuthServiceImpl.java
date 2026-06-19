package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.authRequest.GoogleLoginRequest;
import com.example.car_rental.dto.requestDTO.authRequest.LoginRequest;
import com.example.car_rental.dto.requestDTO.authRequest.RegisterRequest;
import com.example.car_rental.dto.responseDTO.AuthResponse;
import com.example.car_rental.entity.Roles;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.Verifications;
import com.example.car_rental.entity.enums.VerStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.RoleRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.repository.VerificationRepository;
import com.example.car_rental.security.JwtUtils;
import com.example.car_rental.service.AuthService;
import com.example.car_rental.service.VerificationService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, Object> redisTemplate;
    private final VerificationRepository verificationRepository;
    private final VerificationServiceImpl verificationService;

    @Value("${clientId}")
    private String clientId;

    @Transactional
    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {

        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String role = user.getRole().getRoleName();

        String accessToken = jwtUtils.generateAccessToken(String.valueOf(user.getEmail()), role);
        String refreshToken = jwtUtils.generateRefreshToken(String.valueOf(user.getEmail()), role);

        // LƯU REDIS
        redisTemplate.opsForValue().set(
                "refreshToken:" + user.getEmail(),
                refreshToken,
                Duration.ofDays(7));

        // COOKIE
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return mapToAuthResponse(user, accessToken);
    }

    @Override
    @Transactional
    public AuthResponse register(@Valid RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        Users user = Users.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDate.now())
                .isActive(true)
                .role(roleRepository.findByRoleName("USER")
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                .build();

        userRepository.save(user);

        Verifications verifications = Verifications.builder()
                .status(VerStatusEnum.UNVERIFIED)
                .user(user)
                .build();

        verificationRepository.save(verifications);
        verificationService.createNotificationVerification(user.getId(), VerStatusEnum.UNVERIFIED,
                verifications.getId());

        return mapToAuthResponse(user, null);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request);
        if (refreshToken != null) {
            try {
                Claims claims = jwtUtils.parseToken(refreshToken);
                String email = claims.getSubject();
                ;
                redisTemplate.delete("refreshToken:" + email);
                log.info("User {} logout → refresh token deleted", email);
            } catch (Exception e) {
                log.warn("Logout not success");
            }
        }
        // Xóa cookie ở trình duyệt
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    }

    @Override
    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = getRefreshToken(request);
        if (refreshToken == null) {
            throw new AppException(ErrorCode.TOKEN_MISSING);
        }

        try {
            Claims claims = jwtUtils.parseToken(refreshToken);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            // Check Redis
            String redisKey = "refreshToken:" + email;
            String savedToken = (String) redisTemplate.opsForValue().get(redisKey);

            if (savedToken == null || !savedToken.equals(refreshToken)) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }

            String newRefreshToken = jwtUtils.generateRefreshToken(email, role);
            Claims newClaims = jwtUtils.parseToken(newRefreshToken);
            long ttl = newClaims.getExpiration().getTime() - System.currentTimeMillis();

            redisTemplate.opsForValue().set(
                    "refreshToken:" + email,
                    newRefreshToken,
                    Duration.ofMillis(ttl));

            ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(Duration.ofMillis(ttl))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            String newAccessToken = jwtUtils.generateAccessToken(email, role);

            return AuthResponse.builder()
                    .email(email)
                    .token(newAccessToken)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            log.info("REFRESH TOKEN = " + refreshToken);
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }

    @Override
    public AuthResponse loginWithGoogle(GoogleLoginRequest request, HttpServletResponse response) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(request.getTokenId());
            if (googleIdToken == null) {
                throw new AppException(ErrorCode.INVALID_CREDENTIALS);
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            Users user = userRepository.findByEmail(email).orElseGet(() -> {

                Users newUser = Users.builder()
                        .email(email)
                        .fullName(name)
                        .avatarUrl(pictureUrl)
                        .password(passwordEncoder.encode("GOOGLE_LOGIN"))
                        .createdAt(LocalDate.now())
                        .isActive(true)
                        .role(roleRepository.findByRoleName("USER")
                                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                        .build();

                Users savedUser = userRepository.save(newUser);

                Verifications verification = Verifications.builder()
                        .status(VerStatusEnum.UNVERIFIED)
                        .user(savedUser)
                        .build();

                verificationRepository.save(verification);
                verificationService.createNotificationVerification(savedUser.getId(), VerStatusEnum.UNVERIFIED,
                        verification.getId());
                return savedUser;
            });

            String role = user.getRole().getRoleName();
            String accessToken = jwtUtils.generateAccessToken(email, role);
            String refreshToken = jwtUtils.generateRefreshToken(email, role);

            // Lưu Redis whitelist
            redisTemplate.opsForValue().set(
                    "refreshToken:" + user.getEmail(),
                    refreshToken,
                    Duration.ofDays(7));

            // Set HttpOnly Cookie
            ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Strict")
                    .maxAge(Duration.ofDays(7))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return mapToAuthResponse(user, accessToken);

        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public String getRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public AuthResponse mapToAuthResponse(Users user, String token) {
        return AuthResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .roleName(user.getRole().getRoleName())
                .token(token)
                .isActive(user.getIsActive())
                .isGoogleLogin(passwordEncoder.matches("GOOGLE_LOGIN", user.getPassword()))
                .build();
    }

}
