package com.nordic.backend.company.features.news.service;

import com.nordic.backend.company.Common.excetions.UnauthorizedException;
import com.nordic.backend.company.Common.utils.CommonJWTChecker;
import com.nordic.backend.company.features.authorization.service.JwtService;
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
  private final CommonJWTChecker commonJWTChecker;
  private final  JwtService jwtService;

  public ResponseEntity<?> saveNews(NewsModel newsModel, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      return ResponseEntity.ok(newsRepository.save(newsModel));
    } catch (Exception e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }

  public ResponseEntity<?> updateNews(NewsModel newsModel, Long id, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      NewsModel news = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found"));
      news.setTitle(newsModel.getTitle());
      news.setContent(newsModel.getContent());
      news.setThumbnail(newsModel.getThumbnail());
      return ResponseEntity.ok(newsRepository.save(news));

    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  public ResponseEntity<?> deleteNews(Long id, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      newsRepository.deleteById(id);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  public ResponseEntity<?> getAllNews(String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      return ResponseEntity.ok(newsRepository.findAll());
    } catch (Exception e) {
      throw new UnauthorizedException(e.getMessage());}
  }

  public ResponseEntity<?> getNewsById(Long id, String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      return ResponseEntity.ok(newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found")));
    } catch (RuntimeException e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }

  public String uploadNewsPhoto(MultipartFile file, Long newsId,String token, String email) {
    try {
      commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
      NewsModel news = newsRepository.findById(newsId)
          .orElseThrow(() -> new RuntimeException("News not found"));
      String fileUrl = supabaseFileUploadNews.uploadFile(file);
      news.setThumbnail(fileUrl);
      newsRepository.save(news);
      return fileUrl;
    } catch (RuntimeException e) {
      throw new UnauthorizedException(e.getMessage());
    }
  }

}
