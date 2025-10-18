package com.nordic.backend.company.features.authorization.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nordic.backend.company.Common.Responses.JwtResponse;
import com.nordic.backend.company.features.authorization.service.JwtService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
// @NoArgsConstructor
@RestController
@RequestMapping("/api/v1/jwt")
public class JwtController {
  private final JwtService jwtUtil;

  @GetMapping("/refresh")
  public JwtResponse getNewAccessToken(@RequestParam String email, @RequestBody JwtResponse jwtResponse) {

    try {
      JwtResponse newJwtResponse = jwtUtil.getNewAccessToken(jwtResponse.getRefreshToken(), jwtResponse.getAccessToken(), email);
      return newJwtResponse;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}