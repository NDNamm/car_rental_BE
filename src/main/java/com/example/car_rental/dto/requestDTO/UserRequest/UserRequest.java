package com.example.car_rental.dto.requestDTO.UserRequest;

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
    private String fullName;
    private Date dob;
    private GenderEnum gender;
    private String phoneNumber;
    private LocalDate createdAt;
}
