package com.nordic.backend.company.features.news.controller;

import com.nordic.backend.company.features.news.model.NewsModel;
import com.nordic.backend.company.features.news.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(
            @RequestHeader("Authorization") String token,
            @RequestParam String email,
            @PathVariable Long id) {
        return newsService.getNewsById(id, token, email);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllNews(
            @RequestHeader("Authorization") String token,
            @RequestParam String email) {
        return newsService.getAllNews(token, email);
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteNewsById(
            @RequestHeader("Authorization") String token,
            @RequestParam String email,
            @PathVariable Long id) {
        return newsService.deleteNews(id, token, email);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateNewsById(
            @RequestHeader("Authorization") String token,
            @RequestParam String email,
            @PathVariable Long id,
            @RequestBody NewsModel newsModel) {
        return newsService.updateNews(newsModel, id, token, email);
    }

    @PostMapping("/new/add")
    public ResponseEntity<?> addNews(
            @RequestHeader("Authorization") String token,
            @RequestBody NewsModel newsModel,
            @RequestParam String email) {
        return newsService.saveNews(newsModel, token, email);
    }

    @PostMapping("/upload-photo/{newsId}")
    public ResponseEntity<String> uploadPhoto(
            @RequestBody MultipartFile file,
            @RequestHeader("Authorization") String token,
            @PathVariable Long newsId,
            @RequestParam String email) {

        String publicUrl = newsService.uploadNewsPhoto(file, newsId, token, email);
        return ResponseEntity.ok(publicUrl);
    }
}
