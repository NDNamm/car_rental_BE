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
import jakarta.transaction.Transactional;
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
    public Page<UserResponse> getAllUsers(String search, String role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Users> usersPage = null;

        boolean noFilter =
                (search == null || search.isBlank()) &&
//                        (fullName == null || fullName.isBlank()) &&
//                        (phoneNumber == null || phoneNumber.isBlank()) &&
                        (role == null || role.isBlank());

        if (noFilter) {
            usersPage = userRepository.findAll(pageable);
        } else {
            usersPage = userRepository.finByUser(search, role, pageable);
        }

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
    @Transactional
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
    @Transactional
    public UserResponse add(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
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
                .roleName(user.getRole().getRoleName())
                .build();
    }

    @Override
    @Transactional
    public void editProfile(UserRequest userRequest) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

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
    @Transactional
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

                Map<String, String> avatarUrl = imageService.uploadImages(avatar, "users", user.getFullName(), "avatar");
                user.setAvatarUrl(avatarUrl.get("url"));
                user.setPublicIdAvatar(avatarUrl.get("public_id"));
            }
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }
        userRepository.save(user);
    }
}
