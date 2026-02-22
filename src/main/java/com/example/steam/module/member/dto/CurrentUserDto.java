package com.example.steam.module.member.dto;

import com.example.steam.module.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDto {
    private Long id;
    private String email;
    private String nickName;
    private String avatarUrl;

    public static CurrentUserDto from(Member member) {
        return CurrentUserDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickName(member.getNickname())
                .avatarUrl(member.getAvatarUrl())
                .build();
    }
}
