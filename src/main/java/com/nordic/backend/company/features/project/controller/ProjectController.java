package com.nordic.backend.company.features.project.controller;

import com.nordic.backend.company.features.project.model.ProjectModel;
import com.nordic.backend.company.features.project.service.ProjectService;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5173", "http://localhost:5174"}) // or "*" for all
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping("/all")
  public ResponseEntity<?> getAllProjects(
      @RequestHeader("Authorization") String token,
      @RequestParam String email) {
    return projectService.getAllProjects(token, email);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProjectById(
      @RequestHeader("Authorization") String token,
      @RequestParam String email,
      @PathVariable Long id) {
    return projectService.getProjectById(id, token, email);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> updateProjectById(
      @RequestHeader("Authorization") String token,
      @RequestParam String email,
      @PathVariable Long id,
      @RequestBody ProjectModel projectModel) {
    return projectService.updateProject(projectModel, id, token, email);
  }

  @DeleteMapping("/del/{id}")
  public ResponseEntity<?> deleteProjectById(
      @RequestHeader("Authorization") String token,
      @RequestParam String email,
      @PathVariable Long id) {
    return projectService.deleteProject(id, token, email);
  }

  @PostMapping("/new/add")
  public ResponseEntity<?> saveProject(
      @RequestHeader("Authorization") String token,
      @RequestParam String email,
      @RequestBody ProjectModel projectModel) {
    return projectService.saveProject(projectModel, token, email);
  }

  @PostMapping("/upload-photo/{projectId}")
  public ResponseEntity<String> uploadPhoto(
      @RequestHeader("Authorization") String token,
      @RequestParam String email,
      @RequestBody MultipartFile file,
      @PathVariable Long projectId) {
    return ResponseEntity.ok(projectService.uploadProjectPhoto(file, projectId, token, email));
  }
}
