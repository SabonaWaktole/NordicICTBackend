package com.nordic.backend.company.features.admin.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String imageurl;
}
