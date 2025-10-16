package com.nordic.backend.company.features.admin.common.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
