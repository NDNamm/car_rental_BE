package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.Customer.CustomerTopResponse;
import com.example.car_rental.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerDashboardRepository extends JpaRepository<Users, Long> {

    @Query("""
                SELECT COUNT(u)
                FROM Users u
                WHERE u.role.roleName = 'USER'
            """)
    Long getTotalCustomers();

    @Query("SELECT COUNT(u) FROM Users u " +
            "WHERE u.role.roleName = 'USER'" +
            "AND u.isActive = true")
    Long countActiveUsers();

    @Query("SELECT COUNT(u) FROM Users u " +
            "WHERE u.role.roleName = 'USER'" +
            "AND u.isActive = false")
    Long countInactiveUsers();

    // TOP SPENDING
    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Customer.CustomerTopResponse(
                    b.user.email,
                    b.user.fullName,
                    COUNT(b),
                    COALESCE(SUM(b.totalPrice),0)
                )
                FROM Bookings b
                WHERE b.status = 'COMPLETED'
                GROUP BY b.user.id, b.user.email, b.user.fullName
                ORDER BY SUM(b.totalPrice) DESC
            """)
    List<CustomerTopResponse> getTopSpendingCustomers();

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Customer.CustomerTopResponse(
                    b.user.email,
                    b.user.fullName,
                    COUNT(b),
                    COALESCE(SUM(b.totalPrice), 0)
                )
                FROM Bookings b
                WHERE b.status = 'COMPLETED'
                  AND (:from IS NULL OR b.createdAt >= :from)
                  AND (:to IS NULL OR b.createdAt <= :to)
                GROUP BY b.user.id, b.user.email, b.user.fullName
                ORDER BY SUM(b.totalPrice) DESC
            """)
    List<CustomerTopResponse> getTopSpendingCustomers(LocalDateTime from, LocalDateTime to);

    // TOP CANCELLED
    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Customer.CustomerTopResponse(
                    b.user.email,
                    b.user.fullName,
                    COUNT(b),
                    COALESCE(SUM(b.totalPrice),0)
                )
                FROM Bookings b
                WHERE b.status = 'CANCELLED'
                GROUP BY b.user.id, b.user.email, b.user.fullName
                ORDER BY COUNT(b) DESC
            """)
    List<CustomerTopResponse> getTopCancelledCustomers();

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Customer.CustomerTopResponse(
                    b.user.email,
                    b.user.fullName,
                    COUNT(b),
                    COALESCE(SUM(b.totalPrice), 0)
                )
                FROM Bookings b
                WHERE b.status = 'CANCELLED'
                  AND (:from IS NULL OR b.createdAt >= :from)
                  AND (:to IS NULL OR b.createdAt <= :to)
                GROUP BY b.user.id, b.user.email, b.user.fullName
                ORDER BY COUNT(b) DESC
            """)
    List<CustomerTopResponse> getTopCancelledCustomers(LocalDateTime from, LocalDateTime to);
}