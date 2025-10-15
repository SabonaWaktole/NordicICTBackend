package com.nordic.backend.company.Common.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;

@Service
@AllArgsConstructor
public class CommonJWTChecker {
    private final JwtUtil jwtUtil;

    public boolean validateToken(String role, @NotNull String token) {
        if (token.startsWith("Bearer")){
            token = token.substring(7);
        }
        //2 validate token
        if (!jwtUtil.validateJwtToken(token)){
            System.out.println(token);
            return false;
        }
        //3 check role
        return (jwtUtil.getRoleFromToken(token).equals(role) || role == null);
    }

}
