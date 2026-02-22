package com.example.steam.module.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ArticleWriteForm {
    private Long galleryId;
    private String galleryName;
    private String title;
    private String content;

    public static ArticleWriteForm of(String galleryName){
        return ArticleWriteForm.builder()
                .galleryName(galleryName)
                .build();
    }

    public static ArticleWriteForm of(String galleryName, String title, String content) {
        return ArticleWriteForm.builder()
                .galleryName(galleryName)
                .title(title)
                .content(content)
                .build();
    }
}
