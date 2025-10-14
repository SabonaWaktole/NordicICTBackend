package com.nordic.backend.company.features.partner.repository;

import com.nordic.backend.company.features.partner.model.PartnerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<PartnerModel,Long> {
}
