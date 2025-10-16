package com.nordic.backend.company.features.authorization.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.nordic.backend.company.Common.Responses.JwtResponse;
import com.nordic.backend.company.Common.excetions.ExpiredJWTException;
import com.nordic.backend.company.Common.excetions.MalformedJWTException;
import com.nordic.backend.company.Common.excetions.UnauthorizedException;
import com.nordic.backend.company.features.authorization.model.JwtModel;
import com.nordic.backend.company.features.authorization.repository.JwtRepository;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// TODO: Understand and add refresher token

@Component
public class JwtService {
  @Value("${jwt.secret}")
  private String jwtSecret;
  @Value("${jwt.accessexpiration}")
  private int jwtAccessExpirationMS;

  @Value("${jwt.refreshexpiration}")
  private int jwtRefreshExpirationMS;

  private final JwtRepository jwtRepository;

  public JwtService(JwtModel jwtModel, JwtRepository jwtRepository) {
    this.jwtRepository = jwtRepository;
  }

  private SecretKey key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(String username, String role) {
    return Jwts.builder()
        .setSubject(username)
        .claim("role", "ROLE_" + role)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtAccessExpirationMS))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateRefreshToken(String username, String role) {
    Date issuedAt = new Date();
    Date expiration = new Date(issuedAt.getTime() + jwtRefreshExpirationMS);

    String refreshToken = Jwts.builder()
        .setSubject(username)
        .claim("role", "ROLE_" + role)
        .setIssuedAt(issuedAt)
        .setExpiration(expiration)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();

    JwtModel jwtModel = new JwtModel(); // Create a new instance each time
    jwtModel.setToken(refreshToken);
    jwtModel.setUseremail(username);
    jwtModel.setIssuedat(issuedAt);
    jwtModel.setExpiresat(expiration);
    jwtRepository.save(jwtModel);
    return refreshToken;
  }

  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key).build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public String getRoleFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key).build()
        .parseClaimsJws(token)
        .getBody()
        .get("role", String.class);
  }

  public JwtResponse getNewAccessToken(String refreshToken, String accessToken, String userEmail) {
    JwtModel jwtModel = jwtRepository.findByToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("Refresh token not found"));

    // Make sure the token belongs to this user
    if (!jwtModel.getUseremail().equals(userEmail) ||
        getUsernameFromToken(refreshToken) != userEmail ||
        getRoleFromToken(refreshToken) != getRoleFromToken(accessToken)) {
      throw new RuntimeException("Refresh token does not belong to this user");
    }

    try{
      validateJwtToken(refreshToken, userEmail);
    } catch (ExpiredJWTException e) {
      jwtRepository.delete(jwtModel);
      throw new RuntimeException("Refresh token is Expired");
    } catch (MalformedJWTException e) {
      throw new RuntimeException( "Refresh token is malformed");
    }


    // Extract info from the refresh token
    String username = getUsernameFromToken(refreshToken);
    String role = getRoleFromToken(refreshToken);

    // Generate new access token
    String newAccessToken = generateAccessToken(username, role);

    // Delete the old refresh token
    jwtRepository.delete(jwtModel);

    // Generate new refresh token
    String newRefreshToken = generateRefreshToken(username, role);

    return new JwtResponse(newAccessToken, newRefreshToken);
  }

  public boolean validateJwtToken(String token, String userName) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      if (!getUsernameFromToken(token).equals(userName)) {
        throw new UnauthorizedException("Invalid User:");
      }
      return true;
    } catch (SecurityException e) {
      System.out.println("Invalid JWT signature: " + e.getMessage());
      throw new UnauthorizedException("Invalid JWT signature: " + e.getMessage());
    } catch (MalformedJwtException e) {
      System.out.println("Invalid JWT token: " + e.getMessage() + token);
      throw new MalformedJwtException("Invalid JWT token: " + e.getMessage());
    } catch (ExpiredJwtException e) {
      System.out.println("JWT token is expired: " + e.getMessage());
      throw new ExpiredJWTException("JWT token is expired: " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.out.println("JWT token is unsupported: " + e.getMessage());
      throw new UnsupportedJwtException(e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("JWT claims string is empty: " + e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public String logout(String refreshToken) {
    JwtModel jwtModel = jwtRepository.findByToken(refreshToken)
        .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    jwtRepository.delete(jwtModel);
    return "Logout successful";
  }

  public boolean isTokenExpired(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return false;
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

}