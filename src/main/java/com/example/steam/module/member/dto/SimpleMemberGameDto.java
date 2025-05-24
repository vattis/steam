package com.example.steam.module.member.dto;

import com.example.steam.module.member.domain.MemberGame;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SimpleMemberGameDto {
    private Long id;
    private Long gameId;
    private String gameName;
    private int playTime;
    private LocalDateTime lastPlayTime;
    private String gameImageUrl;

    public static SimpleMemberGameDto of(Long id, Long gameId, String gameName, int playTime, LocalDateTime lastPlayTime, String gameImageUrl){
        return SimpleMemberGameDto.builder()
                .id(id)
                .gameId(gameId)
                .gameName(gameName)
                .playTime(playTime)
                .lastPlayTime(lastPlayTime)
                .gameImageUrl(gameImageUrl)
                .build();
    }
    public static SimpleMemberGameDto from(MemberGame memberGame){
        return SimpleMemberGameDto.of(memberGame.getId(), memberGame.getProduct().getId(), memberGame.getName(), memberGame.getPlayMinutes(), memberGame.getLastPlayedTime(), memberGame.getImageUrl());
    }
}
