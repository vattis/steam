package com.example.steam.module.comment.dto;

import com.example.steam.module.comment.domain.ArticleComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCommentDto {
    private Long id;
    private String content;
    private Long memberId;
    private String nickname;
    private String avatarUrl;
    private LocalDateTime createdTime;

    public static ArticleCommentDto from(ArticleComment articleComment){
        return ArticleCommentDto.builder()
                .id(articleComment.getId())
                .content(articleComment.getContent())
                .memberId(articleComment.getMember().getId())
                .nickname(articleComment.getMember().getNickname())
                .avatarUrl(articleComment.getMember().getAvatarUrl())
                .createdTime(articleComment.getCreatedTime())
                .build();
    }
}
