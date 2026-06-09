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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/v1/webhooks/**").permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cars/**",
                                "/api/categories/**",
                                "/api/features/**",
                                "/api/reviews/**",
                                "/api/reviews/car/**")
                        .permitAll()
                        .requestMatchers("/api/users/me/change-password").hasAnyRole("USER", "ADMIN", "STAFF")

                        .requestMatchers("/api/users/me/**", "/api/notifications/**")
                        .hasAnyRole("USER", "ADMIN", "STAFF")

                        .requestMatchers(
                                "/api/verification/me/**",
                                "/api/bookings/me/**",
                                "/api/payments/**",
                                "/api/refunds/me/**",
                                "/api/reviews/me/**")
                        .hasAnyRole("USER", "ADMIN")

                        .requestMatchers(
                                "/api/verification/**",
                                "/api/bookings/**",
                                "/api/refunds/**",
                                "/api/reviews/**")
                        .hasAnyRole("ADMIN", "STAFF")

                        .requestMatchers(
                                "/api/users/**",
                                "/api/cars/**",
                                "/api/categories/**",
                                "/api/features/**",
                                "/api/penalty-rules/**",
                                "/api/dashboard/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}