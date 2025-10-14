package com.nordic.backend.company.features.news.service;


import com.nordic.backend.company.features.news.common.supabase.SupabaseFileUploadNews;
import com.nordic.backend.company.features.news.model.NewsModel;
import com.nordic.backend.company.features.news.repository.NewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final SupabaseFileUploadNews supabaseFileUploadNews;

    public ResponseEntity<?> saveNews(NewsModel newsModel) {
        return ResponseEntity.ok(newsRepository.save(newsModel));
    }

    public ResponseEntity<?> updateNews(NewsModel newsModel, Long id) {
        try {
            NewsModel news = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found"));
            news.setTitle(newsModel.getTitle());
            news.setContent(newsModel.getContent());
            news.setThumbnail(newsModel.getThumbnail());
            return ResponseEntity.ok(newsRepository.save(news));

        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> deleteNews(Long id) {
        try {
            newsRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> getAllNews() {
        return ResponseEntity.ok(newsRepository.findAll());
    }

    public ResponseEntity<?> getNewsById(Long id){
        return  ResponseEntity.ok(newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found")));
    }

    public String uploadNewsPhoto(MultipartFile file, Long newsId){
        NewsModel news = newsRepository.findById(newsId)
                .orElseThrow(() -> new RuntimeException("News not found"));
        String fileUrl = supabaseFileUploadNews.uploadFile(file);
        news.setThumbnail(fileUrl);
        newsRepository.save(news);
        return fileUrl;
    }

}
