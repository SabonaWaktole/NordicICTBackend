package com.nordic.backend.company.features.project.service;

import com.nordic.backend.company.features.project.model.ProjectModel;
import com.nordic.backend.company.features.project.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nordic.backend.company.Common.excetions.UnauthorizedException;
import com.nordic.backend.company.Common.utils.CommonJWTChecker;
import com.nordic.backend.company.features.authorization.service.JwtService;
import com.nordic.backend.company.features.project.common.supabase.SupabaseFileUploadProjects;

@Service
@AllArgsConstructor
public class ProjectService {
  private final ProjectRepository projectRepository;
  private final SupabaseFileUploadProjects supabaseFileUploadProjects;
  private final CommonJWTChecker commonJWTChecker;
  private final JwtService jwtService;

  public ResponseEntity<?> saveProject(ProjectModel projectModel, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      return ResponseEntity.ok(projectRepository.save(projectModel));
    } catch (Exception e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }

  public ResponseEntity<?> getAllProjects(String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      return ResponseEntity.ok(projectRepository.findAll());
    } catch (Exception e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }

  public ResponseEntity<?> getProjectById(Long id, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      return ResponseEntity.ok(projectRepository.findById(id).orElse(null));
    } catch (Exception e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }

  public ResponseEntity<?> updateProject(ProjectModel projectModel, Long id, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      ProjectModel project = projectRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Project not found"));
      project.setTitle(projectModel.getTitle());
      project.setDescription(projectModel.getDescription());
      project.setLogourl(projectModel.getLogourl());
      project.setWeblink(projectModel.getWeblink());
      return ResponseEntity.ok(projectRepository.save(project));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  public ResponseEntity<?> deleteProject(Long id, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      projectRepository.deleteById(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }

  public String uploadProjectPhoto(MultipartFile file, Long projectId, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      ProjectModel projectModel = projectRepository.findById(projectId)
          .orElseThrow(() -> new RuntimeException("Project not found"));
      String logourl = supabaseFileUploadProjects.uploadFile(file);
      projectModel.setLogourl(logourl);
      projectRepository.save(projectModel);
      return logourl;
    } catch (Exception e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }
}
