package com.example.steam.module.article.dto;

import com.example.steam.module.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorNickname;
    private LocalDateTime createdTime;
    private int viewCount;

    public static ArticleDto from(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorId(article.getMember().getId())
                .authorNickname(article.getMember().getNickname())
                .createdTime(article.getCreated())
                .viewCount(article.getViewCount())
                .build();
    }
}
