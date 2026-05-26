package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.CarDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.CustomerResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.entity.Cars;
import com.example.car_rental.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDashboardRepository extends JpaRepository<Users, Long> {

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.CustomerResponse(
                    b.user.email,
                    b.user.fullName,
                    COUNT(b.id),
                    SUM(b.totalPrice)
                )
                FROM Bookings b
                WHERE b.status = 'COMPLETED'
                GROUP BY b.user.id, b.user.fullName, b.user.email
                ORDER BY COUNT(b.id) DESC
            """)
    Page<CustomerResponse> getTopCustomers(Pageable pageable);

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.CustomerResponse(
                     b.user.email,
                     b.user.fullName,
                    COUNT(b.id),
                    SUM(b.totalPrice)
                )
                FROM Bookings b
                WHERE b.status = 'COMPLETED'
                GROUP BY b.user.id, b.user.fullName, b.user.email
                ORDER BY COALESCE(SUM(b.totalPrice), 0) DESC
            """)
    Page<CustomerResponse> getTopSpendingCustomers(Pageable pageable);

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.CustomerResponse(
                     b.user.email,
                     b.user.fullName,
                    COUNT(b.id),
                    SUM(b.totalPrice)
                )
                FROM Bookings b
                WHERE b.status = 'CANCELLED'
                GROUP BY b.user.id, b.user.fullName, b.user.email
                ORDER BY COUNT(b.id) DESC
            """)
    Page<CustomerResponse> getTopCustomerCancelled(Pageable pageable);

    @Query("""
                SELECT COUNT(u)
                FROM Users u
                WHERE u.role.roleName = 'USER'
            """)
    Long getTotalCustomers();
}
