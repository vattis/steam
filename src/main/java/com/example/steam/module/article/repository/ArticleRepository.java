package com.example.steam.module.article.repository;

import com.example.steam.module.article.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllOrderByCreatedAtDesc(Pageable pageable);
}
