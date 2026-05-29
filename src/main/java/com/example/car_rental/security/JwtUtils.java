package com.example.car_rental.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    //    private static final long EXPIRATION_TIME_ACCESS = 15 * 60 * 1000;
    private static final long EXPIRATION_TIME_ACCESS = 7 * 24 * 60 * 60 * 1000;
    private static final long EXPIRATION_TIME_REFRESH = 7 * 24 * 60 * 60 * 1000;

    private Key key;
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secretKey.getBytes();
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 bytes");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /* ================= TOKEN GENERATION ================= */

    public String generateAccessToken(String email, String role) {
        return buildToken(email, role, EXPIRATION_TIME_ACCESS);
    }

    public String generateRefreshToken(String email, String role) {
        String jti = UUID.randomUUID().toString();
        String token = Jwts.builder()
                .setId(jti)
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_REFRESH))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisTemplate.opsForValue().set(
                "refresh:" + email + ":" + jti,
                token,
                Duration.ofMillis(EXPIRATION_TIME_REFRESH)
        );

        return token;
    }

    private String buildToken(String email, String role, long expiration) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /* ================= TOKEN PARSE ================= */

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return parseToken(token).getExpiration();
    }

    /* ================= BLACKLIST ================= */

    public boolean isTokenBlacklisted(String token) {
        try {
            String jti = parseToken(token).getId();
            return redisTemplate.hasKey("blacklist:" + jti);
        } catch (Exception e) {
            return true;
        }
    }

    public void blacklistToken(String token) {
        Claims claims = parseToken(token);
        long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();

        redisTemplate.opsForValue().set(
                "blacklist:" + claims.getId(),
                "revoked",
                Duration.ofMillis(ttl)
        );
    }

    /* ================= VALIDATION ================= */

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            if (isTokenBlacklisted(token)) return false;

            String username = extractUsername(token);
            Date expiration = extractExpiration(token);

            return username.equals(userDetails.getUsername())
                    && expiration.after(new Date());
        } catch (Exception e) {
            log.error("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }
}