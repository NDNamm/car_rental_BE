package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.CarDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.entity.Cars;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarDashboardRepository extends JpaRepository<Cars, Long> {

    @Query("""
                SELECT new  com.example.car_rental.dto.responseDTO.DashboardResponse.CarDashboardResponse(
                    bd.car.name,
                    COUNT(bd.id)
                )
                FROM BookingDetails bd
                GROUP BY bd.car.name
                ORDER BY COUNT(bd.id) DESC
            """)
    Page<CarDashboardResponse> getTopCars(Pageable pageable);

    @Query("""
    SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.CarDashboardResponse(
        bd.car.name,
        COALESCE(SUM(bd.subTotal), 0)
    )
    FROM BookingDetails bd
    WHERE bd.booking.status = 'COMPLETED'
    GROUP BY bd.car.id, bd.car.name
    ORDER BY COALESCE(SUM(bd.subTotal), 0) DESC
""")
    Page<CarDashboardResponse> getTopRevenueCars(Pageable pageable);

    @Query("""
    SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse(
        c.status,
        COUNT(c.id)
    )
    FROM Cars c
    GROUP BY c.status
""")
    List<StatusCountResponse> getCarStatusStatistics();

    @Query("SELECT COUNT(c) FROM Cars c")
    Long getTotalCars();

}
