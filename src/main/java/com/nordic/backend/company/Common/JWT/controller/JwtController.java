package com.nordic.backend.company.Common.JWT.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nordic.backend.company.Common.JWT.service.JwtService;
import com.nordic.backend.company.Common.Responses.JwtResponse;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/jwt")
@AllArgsConstructor
public class JwtController {
  private final JwtService jwtUtil;

  @GetMapping("/refresh")
  public JwtResponse getNewAccesToken(@RequestParam String email, @RequestBody JwtResponse jwtResponse) {

    try {
      JwtResponse newJwtResponse = jwtUtil.getNewAccessToken(jwtResponse.getRefreshToken(), jwtResponse.getAccessToken(), email);
      return newJwtResponse;
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
  


}