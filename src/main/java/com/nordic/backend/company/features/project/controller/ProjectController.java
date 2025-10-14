package com.nordic.backend.company.features.project.controller;

import com.nordic.backend.company.features.project.model.ProjectModel;
import com.nordic.backend.company.features.project.service.ProjectService;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "http://127.0.0.1:5500") // or "*" for all
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProjectById(@PathVariable Long id, @RequestBody ProjectModel projectModel) {
        return projectService.updateProject(projectModel, id);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable Long id) {
        return projectService.deleteProject(id);
    }

    @PostMapping("/new/add")
    public ResponseEntity<?> saveProject(@RequestBody ProjectModel projectModel) {
        return projectService.saveProject(projectModel);
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("projectId") Long projectId) {
        return ResponseEntity.ok(projectService.uploadProjectPhoto(file, projectId));
    }
}
