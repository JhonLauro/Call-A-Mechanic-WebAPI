package com.callamechanic.login.service;

import com.callamechanic.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret:call-a-mechanic-super-secret-key-256-bits!!!!}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}")
    private long expirationMs;

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",     user.getRole());
        claims.put("fullName", user.getFullName());

        if (user.getMechanicId() != null) claims.put("mechanicId", user.getMechanicId());
        if (user.getAdminId()    != null) claims.put("adminId",    user.getAdminId());

        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public void invalidate(String token) {
        // For stateless JWT the client simply discards the token.
        // Add Redis blacklist here if needed in the future.
    }
}