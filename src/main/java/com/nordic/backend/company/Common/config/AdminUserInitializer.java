package com.nordic.backend.company.Common.config;

import com.nordic.backend.company.features.admin.model.AdminModel;
import com.nordic.backend.company.features.admin.repository.AdminRepository;
import com.nordic.backend.company.features.admin.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AdminUserInitializer {
    private final RootConfig rootConfig;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;



    @EventListener(ApplicationReadyEvent.class)
    public void initRootUser() {
        if (!adminService.userExists(rootConfig.getEmail())) {
            AdminModel root = new AdminModel();
            root.setEmail(rootConfig.getEmail());
            root.setPassword(passwordEncoder.encode(rootConfig.getPassword()));
            root.setFirstname(rootConfig.getFirstName());
            root.setLastname(rootConfig.getLastName());
            root.setImageurl(rootConfig.getImageurl());
            root.setRole("ADMIN");
            adminRepository.save(root);
            System.out.println("Root user created successfully!");
        }
    }
}