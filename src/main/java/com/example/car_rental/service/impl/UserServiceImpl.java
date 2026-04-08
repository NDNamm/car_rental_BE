package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.UserRequest.UpdateRoleRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.dto.requestDTO.authRequest.RegisterRequest;
import com.example.car_rental.dto.responseDTO.AuthResponse;
import com.example.car_rental.dto.responseDTO.UserResponse;
import com.example.car_rental.entity.Roles;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.Verifications;
import com.example.car_rental.entity.enums.VerStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.RoleRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.repository.VerificationRepository;
import com.example.car_rental.service.ImageService;
import com.example.car_rental.service.UserService;
import com.example.car_rental.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final ImageService imageService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    //ADMIN
    @Override
    public Page<UserResponse> getAllUsers(String email, String fullName, String phoneNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Users> usersPage = null;

        if (email == null || email.isEmpty() && fullName == null || fullName.isEmpty() && phoneNumber == null || phoneNumber.isEmpty()) {
            usersPage = userRepository.findAll(pageable);
        } else usersPage = userRepository.finByUser(email, fullName, phoneNumber, pageable);


        return usersPage.map(user -> UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .roleName(user.getRole().getRoleName())
                .createdAt(user.getCreatedAt())
                .build());
    }

    @Override
    public void updateRole(Long id, UpdateRoleRequest request) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Roles role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public UserResponse add(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String roleName = request.getRoleName();


        Users user = Users.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDate.now())
                .role(roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                .build();

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roleName(user.getRole().getRoleName())
                .build();
    }

    //USER
    @Override
    public UserResponse getProfile() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarUrl())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .gender(user.getGender())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public void editProfile(UserRequest userRequest) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Verifications verification = verificationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_VERIFIED));

        if (verification != null &&
                VerStatusEnum.APPROVED.equals(verification.getStatus()) &&
                userRequest.getFullName() != null) {

            throw new AppException(ErrorCode.CANNOT_UPDATE_NAME_AFTER_VERIFICATION);
        }

        if (userRequest.getFullName() != null) {
            user.setFullName(userRequest.getFullName());
        }

        if (userRequest.getPhoneNumber() != null) {
            user.setPhoneNumber(userRequest.getPhoneNumber());
        }

        if (userRequest.getDob() != null) {
            user.setDob(userRequest.getDob());
        }

        if (userRequest.getGender() != null) {
            user.setGender(userRequest.getGender());
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDate.now());
        }
        userRepository.save(user);
    }

    @Override
    public void editAvatar(MultipartFile avatar) throws IOException {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        try {
            if (avatar != null && !avatar.isEmpty()) {
                if (user.getAvatarUrl() != null && user.getPublicIdAvatar() != null) {
                    imageService.deleteImages(user.getPublicIdAvatar());
                }

                Map<String, String> cccdFrontUrl = imageService.uploadImages(avatar, "users", user.getFullName(), "avatar");
                user.setAvatarUrl(cccdFrontUrl.get("url"));
                user.setPublicIdAvatar(cccdFrontUrl.get("public_id"));
            }
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }
        userRepository.save(user);
    }
}
