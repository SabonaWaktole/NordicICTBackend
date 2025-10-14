package com.nordic.backend.company.features.news.controller;


import com.nordic.backend.company.features.news.model.NewsModel;
import com.nordic.backend.company.features.news.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "http://127.0.0.1:5500") // or "*" for all
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    private final NewsService newsService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getNewsById(@PathVariable Long id){
        return newsService.getNewsById(id);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllNews(){
        return newsService.getAllNews();
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteNewsById(@PathVariable Long id){
        return newsService.deleteNews(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateNewsById(@PathVariable Long id, @RequestBody NewsModel newsModel){
        return newsService.updateNews( newsModel, id);
    }

    @PostMapping("/new/add")
    public ResponseEntity<?> addNews(@RequestBody NewsModel newsModel){
        return newsService.saveNews(newsModel);
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("newsId") Long newsId) {

        String publicUrl = newsService.uploadNewsPhoto(file, newsId);
        return ResponseEntity.ok(publicUrl);
    }
}
