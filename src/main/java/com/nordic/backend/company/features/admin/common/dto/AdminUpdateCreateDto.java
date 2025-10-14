package com.nordic.backend.company.features.admin.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateCreateDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String role;
    private String imageurl;

}
