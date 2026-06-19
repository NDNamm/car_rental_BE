package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.UserRequest.UpdateRoleRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UpdateUserRequest;
import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.dto.responseDTO.UserResponse;
import com.example.car_rental.entity.Roles;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.RoleRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.repository.VerificationRepository;
import com.example.car_rental.service.ImageService;
import com.example.car_rental.service.NotificationService;
import com.example.car_rental.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ImageService imageService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    // ADMIN
    @Override
    public Page<UserResponse> getAllUsers(String search, String role, Boolean isActive, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Users> usersPage = null;

        boolean noFilter = (search == null || search.isBlank()) &&
                (role == null || role.isBlank()) &&
                (isActive == null);

        if (noFilter) {
            usersPage = userRepository.finByUser(null, null, null, pageable);
        } else {
            usersPage = userRepository.finByUser(search, role, isActive, pageable);
        }

        return usersPage.map(this::mapToUserResponse);
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
    @Transactional
    public void updateUser(Long id, UpdateUserRequest request) {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
            if (Boolean.FALSE.equals(request.getIsActive())) {
                user.setReason(request.getReason());
                createNotificationUser(user);
            } else {
                user.setReason(null);
                createUserActiveNotification(user);
            }
        }

        userRepository.save(user);
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
                .phoneNumber(request.getPhoneNumber())
                .dob(request.getDob())
                .createdAt(LocalDate.now())
                .isActive(true)
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

    // USER
    @Override
    public UserResponse getProfile(String email) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return mapToUserResponse(user);
    }

    @Override
    @Transactional
    public void editProfile(String email, UserRequest userRequest) {

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
    public void editAvatar(String email, MultipartFile avatar) throws IOException {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        try {
            if (avatar != null && !avatar.isEmpty()) {
                if (user.getAvatarUrl() != null && user.getPublicIdAvatar() != null) {
                    imageService.deleteImages(user.getPublicIdAvatar());
                }

                Map<String, String> avatarUrl = imageService.uploadImages(avatar, "users", user.getFullName(),
                        "avatar");
                user.setAvatarUrl(avatarUrl.get("url"));
                user.setPublicIdAvatar(avatarUrl.get("public_id"));
            }
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }
        userRepository.save(user);

    }

    private UserResponse mapToUserResponse(Users user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .roleName(user.getRole().getRoleName())
                .createdAt(user.getCreatedAt())
                .isActive(user.getIsActive())
                .reason(user.getReason())
                .isGoogleLogin(passwordEncoder.matches("GOOGLE_LOGIN", user.getPassword()))
                .build();
    }

    private void createNotificationUser(Users user) {
        String message = "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ với chúng tôi để biết thêm chi tiết.";
        notificationService.createNotification(
                user.getId(),
                NotificationTypeEnum.ACCOUNT,
                message, null);
    }

    private void createUserActiveNotification(Users user) {
        String message = "Tài khoản của bạn đã được kích hoạt lại. Bạn có thể đặt xe ngay bây giờ!";
        notificationService.createNotification(
                user.getId(),
                NotificationTypeEnum.ACCOUNT,
                message, null);
    }
}
