package com.example.car_rental.dto.requestDTO.UserRequest;

import com.example.car_rental.entity.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    private String fullName;
    private String phoneNumber;
    private GenderEnum gender;
    private Boolean isActive;
    private Long roleId;
    private String reason;
    private Date dob;
}