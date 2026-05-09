package com.example.car_rental.dto.responseDTO;

import com.example.car_rental.entity.enums.GenderEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String avatarUrl;
    private Date dob;
    private GenderEnum gender;
    private String email;
    private String phoneNumber;
    private String roleName;
    private LocalDate createdAt;
    private Boolean isActive;
    private String reason;
}
