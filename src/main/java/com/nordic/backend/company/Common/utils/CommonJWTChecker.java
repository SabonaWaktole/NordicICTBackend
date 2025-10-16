package com.nordic.backend.company.Common.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.nordic.backend.company.Common.excetions.UnauthorizedException;
import com.nordic.backend.company.features.authorization.service.JwtService;

import jakarta.validation.constraints.NotNull;

@Service
@AllArgsConstructor
public class CommonJWTChecker {
  private final JwtService jwtUtil;

  public boolean validateToken(String role, @NotNull String token, String email) {
    if (token.startsWith("Bearer")) {
      token = token.substring(7);
    }
    // 2 validate token
    if (!jwtUtil.validateJwtToken(token, email)) {
      throw new UnauthorizedException("Unauthorized User");
    }
    if (!jwtUtil.getUsernameFromToken(token).equals(email)) {
      throw new UnauthorizedException("Unauthorized User");
    }
    // 3 check role
    return (jwtUtil.getRoleFromToken(token).equals(role) || role == null);
  }

}