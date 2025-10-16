package com.nordic.backend.company.features.admin.controller;

import com.nordic.backend.company.Common.Responses.JwtResponse;
import com.nordic.backend.company.features.admin.common.dto.LoginRequest;
import com.nordic.backend.company.features.admin.model.AdminModel;
import com.nordic.backend.company.features.admin.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5173", "http://localhost:5174"}) // or "*" for all
@AllArgsConstructor
// @NoArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

  private final AdminService adminService;


  @GetMapping("/{email}")
  public ResponseEntity<?> getAdmin(@RequestHeader("Authorization") String token,@PathVariable String email) {
    return adminService.getAdminByEmail(token, email);
  }

  @GetMapping("/all")
  public ResponseEntity<?> getAllAdmin(@RequestHeader("Authorization") String token, @RequestBody String email) {
    return adminService.getAllAdmins(token, email);
  }

  @PostMapping("/new")
  public ResponseEntity<?> addNewAdmin(@RequestHeader("Authorization") String token, @RequestParam String email,
      @RequestBody AdminModel admin) {
    return adminService.saveAdmin(admin, token, email);
  }

  @PutMapping("/update/{email}")
  public ResponseEntity<?> updateAdmin(@RequestHeader("Authorization") String token, @PathVariable String email,
      @RequestBody AdminModel admin) {
    return adminService.updateAdmin(admin, token, email);
  }

  @DeleteMapping("/delete/{email}")
  public ResponseEntity<?> deleteAdmin(@RequestHeader("Authorization") String token, @PathVariable String email) {
    return adminService.deleteAdmin(token, email);
  }

  @PostMapping("/upload-photo")
  public ResponseEntity<String> uploadPhoto(
      @RequestHeader("Authorization") String token,
      @RequestBody MultipartFile file,
      @RequestParam("adminEmail") String email) {
    String publicUrl = adminService.uploadAdminPhoto(file, token, email);
    return ResponseEntity.ok(publicUrl);
  }

  @GetMapping("/login")
  public JwtResponse login(@RequestBody LoginRequest request) {
    return adminService.login(request.getEmail(), request.getPassword());
  }

    @DeleteMapping("/logout")
  public String logout(@RequestBody String refreshToken) {

    try {
      adminService.logout(refreshToken);
      return "Logout successful";
    } catch (Exception e) {
      return new String();
  }
  
}
}
