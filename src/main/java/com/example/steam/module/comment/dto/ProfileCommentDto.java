package com.example.steam.module.comment.dto;

import com.example.steam.module.comment.domain.ProfileComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ProfileCommentDto {
    private Long id;
    private String nickName;
    private String content;
    private LocalDateTime createdAt;

    public static ProfileCommentDto of(Long id, String nickName, String content, LocalDateTime createdAt){
        return ProfileCommentDto
                .builder()
                .id(id)
                .nickName(nickName)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
    public static ProfileCommentDto from(ProfileComment profileComment) {
        return ProfileCommentDto.of(
                profileComment.getId(),
                profileComment.getMember().getNickname(),
                profileComment.getContent(),
                profileComment.getCreatedTime());
    }
}
