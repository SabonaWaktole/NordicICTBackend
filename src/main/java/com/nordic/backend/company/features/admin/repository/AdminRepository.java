package com.nordic.backend.company.features.admin.repository;

import com.nordic.backend.company.features.admin.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminModel, Long> {
    AdminModel findByEmail(String email);
}
