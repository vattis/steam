package com.example.steam.module.member.dto;

import com.example.steam.module.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class SimpleMemberDto {
    private Long id;
    private String nickname;

    public static SimpleMemberDto from(Member member){
        return SimpleMemberDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .build();
    }
}
