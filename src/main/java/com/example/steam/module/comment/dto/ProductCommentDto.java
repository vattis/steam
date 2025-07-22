package com.example.steam.module.comment.dto;


import com.example.steam.module.member.dto.SimpleMemberDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class ProductCommentDto {
    private Long id;
    private SimpleMemberDto nickname;
    private String content;
    private LocalDateTime createdTime;
}
