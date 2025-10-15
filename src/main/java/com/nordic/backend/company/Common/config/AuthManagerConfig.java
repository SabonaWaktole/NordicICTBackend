package com.nordic.backend.company.Common.config;
import com.nordic.backend.company.Common.ServiceBeans.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class AuthManagerConfig {

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {

        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder.userDetailsService(customUserDetailsService);

        return authBuilder.build();
    }
}
