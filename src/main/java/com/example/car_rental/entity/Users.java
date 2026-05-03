package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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

    @Column(name = "public_id_avatar")
    private String publicIdAvatar;

    @Column(name = "date_of_birth")
    private Date dob;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "role_id")
    private Roles role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Verifications verification;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
    private CarHandovers carHandovers;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Bookings> myBookings;

    @OneToMany(mappedBy = "changedBy")
    @JsonIgnore
    private List<BookingStatusHistory> changeStatusHistories;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reviews> review;
}
