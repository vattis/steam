package com.example.steam.module.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SimpleMemberGameDto {
    private Long id;
    private Long gameId;
}
