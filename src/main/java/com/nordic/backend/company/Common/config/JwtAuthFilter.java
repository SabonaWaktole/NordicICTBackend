package com.nordic.backend.company.Common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.nordic.backend.company.features.authorization.service.JwtService;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtUtil;

  public JwtAuthFilter(JwtService jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    String path = request.getRequestURI();

    // Skip public endpoints and login
    if (path.startsWith("/api/v1/public") || path.endsWith("/login")) {
      filterChain.doFilter(request, response);
      return;
    }

    // Get Authorization header
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7).trim(); // remove "Bearer " prefix and trim spaces

      // Only parse if token is not empty
      if (!token.isEmpty()) {
        try {
          System.out.println(token + " token sabona");
          String username = jwtUtil.getUsernameFromToken(token);

          if (jwtUtil.validateJwtToken(token, username)) {
            String role = jwtUtil.getRoleFromToken(token);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role)));

            SecurityContextHolder.getContext().setAuthentication(authentication);
          }

        } catch (Exception e) {
          // Optional: log the error for debugging, but don’t break the filter chain
          System.out.println("JWT parsing failed: " + e.getMessage());
        }
      }
    }

    // Continue filter chain
    filterChain.doFilter(request, response);
  }
}
