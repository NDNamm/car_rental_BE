package com.example.car_rental.dto.requestDTO.UserRequest;

import com.example.car_rental.entity.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

    private Long id;
    private String email;
    private String fullName;
    private Date dob;
    private GenderEnum gender;
    private String phoneNumber;
    private LocalDate createdAt;
    private String roleName;
    private String password;
}
