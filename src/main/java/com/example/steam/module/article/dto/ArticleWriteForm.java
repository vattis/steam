package com.example.steam.module.article.dto;

import com.example.steam.module.member.dto.SimpleMemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ArticleWriteForm {
    private Long galleryId;
    private String title;
    private String content;
    private SimpleMemberDto memberDto;
    private LocalDateTime createdDate;

    public static ArticleWriteForm of(Long galleryId, String title, String content, SimpleMemberDto memberDto) {
        return ArticleWriteForm.builder()
                .galleryId(galleryId)
                .title(title)
                .content(content)
                .memberDto(memberDto)
                .build();
    }
}
