package com.example.car_rental.dto.responseDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {
    private int code = 200;
    private String message = "success";
    private T data;
}
