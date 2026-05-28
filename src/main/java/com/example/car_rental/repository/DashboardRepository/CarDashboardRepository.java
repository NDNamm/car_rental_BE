package com.example.car_rental.repository.DashboardRepository;


import com.example.car_rental.dto.responseDTO.DashboardResponse.Cars.CarTopResponse;
import com.example.car_rental.entity.Cars;


import com.example.car_rental.entity.enums.CarStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarDashboardRepository extends JpaRepository<Cars, Long> {

    Long countBy();

    Long countByStatus(CarStatusEnum status);

    @Query("""
        SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Cars.CarTopResponse(
            bd.car.name,
            COUNT(bd),
            COALESCE(SUM(bd.subTotal),0)
        )
        FROM BookingDetails bd
        GROUP BY bd.car.id, bd.car.name
    """)
    List<CarTopResponse> getTopCars();
}