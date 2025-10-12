package com.nordic.backend.company.features.admin.service;

import com.nordic.backend.company.features.admin.model.AdminModel;
import com.nordic.backend.company.features.admin.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminModel saveAdmin(AdminModel adminModel) {
        return adminRepository.save(adminModel);
    }

    public List<AdminModel> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<AdminModel> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public AdminModel getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public AdminModel updateAdmin(Long id, AdminModel adminModel) {
        AdminModel admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Admin not found"));
        admin.setFirstname(adminModel.getFirstname());
        admin.setLastname(adminModel.getLastname());
        admin.setEmail(adminModel.getEmail());
        admin.setPassword(adminModel.getPassword());
        admin.setImageurl(adminModel.getImageurl());
        return adminRepository.save(admin);
    }
}
