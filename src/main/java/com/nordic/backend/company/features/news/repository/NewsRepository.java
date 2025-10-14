package com.nordic.backend.company.features.news.repository;

import com.nordic.backend.company.features.news.model.NewsModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<NewsModel, Long> {
}
