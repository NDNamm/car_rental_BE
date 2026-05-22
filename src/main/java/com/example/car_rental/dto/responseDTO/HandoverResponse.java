package com.example.car_rental.dto.responseDTO;

import com.example.car_rental.entity.BookingDetails;
import com.example.car_rental.entity.HandoverImages;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.CarHandoverEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HandoverResponse {
    private Long id;
    private CarHandoverEnum type;
    private LocalDateTime handoverTime;
    private Long mileage;
    private Integer fuelLevel;
    private String note;
    private LocalDateTime createdAt;
    private String nameStaff;
    private List<String> handoverImages;
    private List<IncidentResponse> incidentResponses;
}
