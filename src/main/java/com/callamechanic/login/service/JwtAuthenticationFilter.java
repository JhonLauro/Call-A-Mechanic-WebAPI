package com.callamechanic.login.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret:call-a-mechanic-super-secret-key-change-this-in-production!}")
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = extractTokenFromHeader(request);

            System.out.println("JWT Filter - Token extracted: " + (token != null ? "Yes" : "No"));
            System.out.println("JWT Filter - Request URI: " + request.getRequestURI());

            if (token != null) {
                Claims claims = validateTokenAndGetClaims(token);

                if (claims != null) {
                    Long userId = Long.parseLong(claims.getSubject());
                    String email = (String) claims.get("email");
                    String role = (String) claims.get("role");
                    String fullName = (String) claims.get("fullName");

                    System.out.println("JWT Filter - Token valid. User ID: " + userId + ", Role: " + role);

                    CustomUserDetails userDetails = new CustomUserDetails(
                            userId,
                            email != null ? email : fullName,
                            "",
                            role
                    );

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("JWT Filter - Token validation failed");
                }
            }
        } catch (Exception ex) {
            System.err.println("JWT Filter Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String headers = request.getHeader("Authorization");

        if (headers != null && headers.startsWith("Bearer ")) {
            return headers.substring(7);
        }

        return null;
    }

    private Claims validateTokenAndGetClaims(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ex) {
            // Token validation failed
            return null;
        }
    }
}
