package com.nordic.backend.company.features.admin.controller;

import com.nordic.backend.company.features.admin.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
//@NoArgsConstructor
@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/{email}")
    public ResponseEntity<?> getAdmin(@PathVariable String email) {
        return adminService.getAdminByEmail(email);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAdmin(){
        return adminService.getAllAdmins();
    }

}
