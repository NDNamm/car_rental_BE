package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "date_of_birth")
    private Date dob;

    @Column(name = "gender")
    private GenderEnum gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "verified")
    private Boolean verified = false;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "role_id")
    private Roles role;
}
