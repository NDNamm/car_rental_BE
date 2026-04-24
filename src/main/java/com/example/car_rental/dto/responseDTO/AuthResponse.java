package com.example.car_rental.dto.responseDTO;

import lombok.*;

import javax.management.relation.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String roleName;
    private String avatarUrl;
    private String token;
}
