package com.nordic.backend.company.features.admin.service;

import com.nordic.backend.company.features.admin.model.AdminModel;
import com.nordic.backend.company.features.admin.repository.AdminRepository;
import com.nordic.backend.company.features.admin.common.supabase.SupabaseFileUploadAdmin;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final SupabaseFileUploadAdmin supabaseFileUpload;

    public ResponseEntity<?> saveAdmin(AdminModel adminModel) {
        return ResponseEntity.ok(adminRepository.save(adminModel));
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
            AdminModel admin = adminRepository.findByEmail(email);
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

}
