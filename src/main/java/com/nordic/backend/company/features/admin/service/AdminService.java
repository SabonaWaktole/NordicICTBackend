package com.nordic.backend.company.features.admin.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nordic.backend.company.Common.JWT.service.JwtService;
import com.nordic.backend.company.Common.Responses.JwtResponse;
import com.nordic.backend.company.Common.excetions.UnauthorizedException;
import com.nordic.backend.company.features.admin.common.supabase.SupabaseFileUploadAdmin;
import com.nordic.backend.company.features.admin.model.AdminModel;
import com.nordic.backend.company.features.admin.repository.AdminRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AdminService {

  private final AdminRepository adminRepository;
  private final SupabaseFileUploadAdmin supabaseFileUpload;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtUtil;

  public ResponseEntity<?> saveAdmin(AdminModel adminModel) {
    adminModel.setPassword(passwordEncoder.encode(adminModel.getPassword()));
    System.out.println(adminModel.getPassword());
    return ResponseEntity.ok(adminRepository.save(adminModel));
  }

  public boolean userExists(String email) {
    return adminRepository.findByEmail(email).isPresent();
  }

  public ResponseEntity<?> getAllAdmins() {
    return ResponseEntity.ok(adminRepository.findAll());
  }

  public ResponseEntity<?> getAdminById(Long id) {
    return ResponseEntity.ok(adminRepository.findById(id).orElse(null));
  }

  public ResponseEntity<?> deleteAdmin(Long id) {
    try {
      adminRepository.deleteById(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  public ResponseEntity<?> getAdminByEmail(String email) {
    try {
      Optional<AdminModel> admin = adminRepository.findByEmail(email);
      return new ResponseEntity<>(admin, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<?> updateAdmin(Long id, AdminModel adminModel) {
    try {
      AdminModel admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
      admin.setFirstname(adminModel.getFirstname());
      admin.setLastname(adminModel.getLastname());
      admin.setEmail(adminModel.getEmail());
      admin.setPassword(adminModel.getPassword());
      admin.setImageurl(adminModel.getImageurl());
      return ResponseEntity.ok(adminRepository.save(admin));
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  public String uploadAdminPhoto(MultipartFile file, Long adminId) {
    AdminModel admin = adminRepository.findById(adminId)
        .orElseThrow(() -> new RuntimeException("Admin not found"));

    // Upload file to Supabase
    String fileUrl = supabaseFileUpload.uploadFile(file);

    // Save public URL in DB
    admin.setImageurl(fileUrl);
    adminRepository.save(admin);

    return fileUrl;
  }

  public JwtResponse login(String email, String password) {
    try {
      Optional<AdminModel> admin = adminRepository.findByEmail(email);
      if (admin.isPresent()) {
        // Cast admin to adminmodel
        AdminModel adminModel = admin.get();

        if (!passwordEncoder.matches(password, adminModel.getPassword())) {
          throw new UnauthorizedException("Invalid email or password");
        }

        String accesToken = jwtUtil.generateAccessToken(adminModel.getEmail(), "ADMIN");
        String refreshToken = jwtUtil.generateRefreshToken(adminModel.getEmail(), "ADMIN");
        return new JwtResponse(accesToken, refreshToken);
      } else {
        throw new UnauthorizedException("Invalid email or password");
      }

    } catch (Exception e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }

  public String getNewAccessToken(String refreshToken, String accesToken, String userEmail) {
    return jwtUtil.getNewAccessToken(refreshToken, accesToken, userEmail);
  }
}
