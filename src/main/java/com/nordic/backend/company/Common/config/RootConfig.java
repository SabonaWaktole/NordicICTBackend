package com.nordic.backend.company.Common.config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "myapp")
public class RootConfig {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String imageurl;
    
}