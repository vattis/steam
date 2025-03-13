package com.example.steam.module.article.dto;

import com.example.steam.module.article.domain.Article;
import com.example.steam.module.member.dto.SimpleMemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private SimpleMemberDto member;

    public static ArticleDto from(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .member(SimpleMemberDto.from(article.getMember()))
                .build();
    }
}
