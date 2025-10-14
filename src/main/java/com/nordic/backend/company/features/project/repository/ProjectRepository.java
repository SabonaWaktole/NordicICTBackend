package com.nordic.backend.company.features.project.repository;

import com.nordic.backend.company.features.project.model.ProjectModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectModel, Long> {
}
