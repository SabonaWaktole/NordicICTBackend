package com.nordic.backend.company.features.admin.service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nordic.backend.company.Common.Responses.JwtResponse;
import com.nordic.backend.company.Common.excetions.UnauthorizedException;
import com.nordic.backend.company.Common.utils.CommonJWTChecker;
import com.nordic.backend.company.features.admin.common.supabase.SupabaseFileUploadAdmin;
import com.nordic.backend.company.features.admin.model.AdminModel;
import com.nordic.backend.company.features.admin.repository.AdminRepository;
import com.nordic.backend.company.features.authorization.service.JwtService;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AdminService {

  private final AdminRepository adminRepository;
  private final SupabaseFileUploadAdmin supabaseFileUpload;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtUtil;
  private final CommonJWTChecker commonJWTChecker;

  public ResponseEntity<?> saveAdmin(AdminModel adminModel, String token, String email) {

    try {
      commonJWTChecker.validateToken("ADMIN", token, email);
      adminModel.setPassword(passwordEncoder.encode(adminModel.getPassword()));
      return ResponseEntity.ok(adminRepository.save(adminModel));
    } catch (Exception e) {
      throw new UnauthorizedException("Unauthorized Admin");
    }

  }

  public boolean userExists(String email) {
    return adminRepository.findByEmail(email).isPresent();
  }

  public ResponseEntity<?> getAllAdmins(String token, String email) {
    try {
      commonJWTChecker.validateToken("ADMIN", token, email);
      return ResponseEntity.ok(adminRepository.findAll());
    } catch (Exception e) {
      throw new UnauthorizedException("Unauthorized Admin");
    }
  }

  public ResponseEntity<?> getAdminById(Long id) {
    return ResponseEntity.ok(adminRepository.findById(id).orElse(null));
  }

  public ResponseEntity<?> deleteAdmin(String token, String email) {
    try {
      commonJWTChecker.validateToken("ADMIN", token, email);
      AdminModel adminModel = adminRepository.findByEmail(email)
          .orElseThrow(() -> new RuntimeException("Admin not found"));
      adminRepository.deleteById(adminModel.getId());
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  public ResponseEntity<?> getAdminByEmail(String token, String email) {
    try {
      commonJWTChecker.validateToken("ADMIN", token, email);
      Optional<AdminModel> admin = adminRepository.findByEmail(email);
      return new ResponseEntity<>(admin, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<?> updateAdmin(AdminModel adminModel, String accessToken, String email) {
    try {
      commonJWTChecker.validateToken("ADMIN", accessToken, email);
      AdminModel admin = adminRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));
      admin.setFirstname(adminModel.getFirstname());
      admin.setLastname(adminModel.getLastname());
      admin.setEmail(adminModel.getEmail());
      admin.setPassword(adminModel.getPassword());
      admin.setImageurl(adminModel.getImageurl());
      return ResponseEntity.ok(adminRepository.save(admin));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  public String uploadAdminPhoto(MultipartFile file, String token, String email) {
    try {
      commonJWTChecker.validateToken("ADMIN", token, email);
      AdminModel admin = adminRepository.findByEmail(email)
          .orElseThrow(() -> new RuntimeException("Admin not found"));

      // Upload file to Supabase
      String fileUrl = supabaseFileUpload.uploadFile(file);

      // Save public URL in DB
      admin.setImageurl(fileUrl);
      adminRepository.save(admin);

      return fileUrl;
    } catch (Exception e) {
      // TODO Auto-generated catch block
      throw new UnauthorizedException(e.getMessage());
    }
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

  // public JwtResponse getNewAccessToken(String refreshToken, String accesToken,
  // String userEmail) {
  // return jwtUtil.getNewAccessToken(refreshToken, accesToken, userEmail);
  // }
  public String logout(String refreshToken) {
    return jwtUtil.logout(refreshToken);
  }
}
