package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.entity.Users;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    //ADMIN
    @Override
    public Page<UserRequest> getAllUsers(String email, String fullName, String phoneNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Users> usersPage = null;

        if (email == null || email.isEmpty() && fullName == null || fullName.isEmpty() && phoneNumber == null || phoneNumber.isEmpty()) {
            usersPage = userRepository.findAll(pageable);
        } else usersPage = userRepository.finByUser(email, fullName, phoneNumber, pageable);


        return usersPage.map(user -> UserRequest.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .avatarUrl(user.getAvatarUrl())
                .gender(user.getGender())
                .createdAt(user.getCreatedAt())
                .build());
    }

    @Override
    public void updateRole(Long id, UserRequest userRequest) {

    }

    @Override
    public void delete(Long id) {

    }


    //USER
    @Override
    public UserRequest editProfile(UserRequest userRequest) {
        return null;
    }
}
