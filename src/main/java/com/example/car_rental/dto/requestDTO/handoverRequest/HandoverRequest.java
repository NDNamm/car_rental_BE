package com.example.car_rental.dto.requestDTO.handoverRequest;

import com.example.car_rental.dto.requestDTO.IncidentRequest;
import com.example.car_rental.entity.enums.CarHandoverEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HandoverRequest {
    private Long bookingDetailId;
    private Long mileage;
    private Integer fuelLevel;
    private String noteHandover;
    private CarHandoverEnum type;
    private List<IncidentRequest> incidentRequests;
}
