package com.nordic.backend.company.Common.ServiceBeans;

import com.nordic.backend.company.features.admin.repository.AdminRepository;
import com.nordic.backend.company.features.admin.model.AdminModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final AdminRepository adminRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    AdminModel user = adminRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return User
        .withUsername(user.getEmail())
        .password(user.getPassword())
        .roles(user.getRole())
        .build();
  }
}
