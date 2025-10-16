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

/**
 * Filter every incoming request to check if the request has a valid JWT token.
 * If the token is valid, authenticate the user and set the authentication in the security context.
 * If the token is invalid, do not authenticate the user.
 * @param request the incoming request
 * @param response the response to the request
 * @param filterChain the filter chain to continue the request
 * @throws ServletException if an error occurs during the filter
 * @throws IOException if an error occurs during the filter
 */
  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    String path = request.getRequestURI();
    if (path.startsWith("/api/v1/public") || path.endsWith("/login")) {
      filterChain.doFilter(request, response);
      return;
    }

    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      String username = jwtUtil.getUsernameFromToken(token);
      if (jwtUtil.validateJwtToken(token, username)) {
        String role = jwtUtil.getRoleFromToken(token);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            username,
            null,
            Collections.singletonList(new SimpleGrantedAuthority(role)));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }

}
