package com.nordic.backend.company.features.admin.controller;

import com.nordic.backend.company.Common.Responses.JwtResponse;
import com.nordic.backend.company.features.admin.common.dto.LoginRequest;
import com.nordic.backend.company.features.admin.model.AdminModel;
import com.nordic.backend.company.features.admin.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@CrossOrigin(origins = "http://127.0.0.1:5500") // or "*" for all
@AllArgsConstructor
//@NoArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
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

    @PostMapping("/new")
    public ResponseEntity<?> addNewAdmin(@RequestBody AdminModel admin) {
        return adminService.saveAdmin(admin);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody AdminModel admin) {
        return adminService.updateAdmin(id, admin);
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        return adminService.deleteAdmin(id);
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("adminId") Long adminId) {

        String publicUrl = adminService.uploadAdminPhoto(file, adminId);
        return ResponseEntity.ok(publicUrl);
    }

    @GetMapping("/login")
    public JwtResponse login(@RequestBody LoginRequest request) {
        return adminService.login(request.getEmail(), request.getPassword());
    }
    
}
