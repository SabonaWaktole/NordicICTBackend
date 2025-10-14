package com.nordic.backend.company.features.project.service;

import com.nordic.backend.company.features.project.model.ProjectModel;
import com.nordic.backend.company.features.project.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.nordic.backend.company.features.project.common.supabase.SupabaseFileUploadProjects;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final SupabaseFileUploadProjects supabaseFileUploadProjects;

    public ResponseEntity<?> saveProject(ProjectModel projectModel) {
        return ResponseEntity.ok(projectRepository.save(projectModel));
    }
    public ResponseEntity<?> getAllProjects() {
        return ResponseEntity.ok(projectRepository.findAll());
    }
    public ResponseEntity<?> getProjectById(Long id) {
        return ResponseEntity.ok(projectRepository.findById(id).orElse(null));
    }
    public ResponseEntity<?> updateProject(ProjectModel projectModel, Long id) {
        try {
            ProjectModel project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
            project.setTitle(projectModel.getTitle());
            project.setDescription(projectModel.getDescription());
            project.setLogourl(projectModel.getLogourl());
            project.setWeblink(projectModel.getWeblink());
            return ResponseEntity.ok(projectRepository.save(project));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    public ResponseEntity<?> deleteProject(Long id) {
        projectRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public String uploadProjectPhoto(MultipartFile file, Long projectId) {
        ProjectModel projectModel = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        String logourl = supabaseFileUploadProjects.uploadFile(file);
        projectModel.setLogourl(logourl);
        projectRepository.save(projectModel);
        return logourl;
    }
}
