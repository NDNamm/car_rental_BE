package com.example.car_rental.config;

import com.example.car_rental.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(csrt -> csrt.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/**", "/api/webhooks/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cars/**", "/api/category/**", "/api/features/**").permitAll()

                        .requestMatchers("/api/verification/me/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/users/me/**").hasAnyRole("USER", "ADMIN","STAFF")
                        .requestMatchers("/api/bookings/me/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/payment/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/refunds/me/**").hasAnyRole("USER", "ADMIN")


                        .requestMatchers("/api/verification/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers("/api/cars/**").hasRole("ADMIN")
                        .requestMatchers("/api/category/**").hasRole("ADMIN")
                        .requestMatchers("/api/features/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
