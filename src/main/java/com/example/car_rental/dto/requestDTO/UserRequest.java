package com.example.car_rental.dto.requestDTO;

import com.example.car_rental.entity.enums.GenderEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private Long id;
    private String userName;
    private String password;
    private String fullName;
    private String avatarUrl;
    private Date dob;
    private GenderEnum gender;
    private String email;
    private String phoneNumber;
    private LocalDate createdAt;
}
