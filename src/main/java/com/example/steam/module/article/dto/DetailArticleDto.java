package com.example.steam.module.article.dto;

import com.example.steam.module.article.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailArticleDto {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorNickname;
    private LocalDateTime createdTime;
    private int viewCount;
    private int likes;

    public static DetailArticleDto from(Article article){
        return DetailArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .authorId(article.getMember().getId())
                .authorNickname(article.getMember().getNickname())
                .createdTime(article.getCreated())
                .viewCount(article.getViewCount())
                .likes(article.getLikes())
                .build();
    }
}
