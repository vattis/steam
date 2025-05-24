package com.example.steam.module.member.dto;

import com.example.steam.module.comment.dto.ProfileCommentDto;
import com.example.steam.module.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    private Long id;
    private String nickName;
    private String avatarUrl;
    private List<SimpleMemberGameDto> simpleMemberGames;
    private Page<ProfileCommentDto> profileCommentPage;

    public static ProfileDto of(Member member, List<SimpleMemberGameDto> simpleMemberGames, Page<ProfileCommentDto> profileCommentPage) {
        return ProfileDto.builder()
                .id(member.getId())
                .nickName(member.getNickname())
                .avatarUrl(member.getAvatarUrl())
                .simpleMemberGames(simpleMemberGames)
                .profileCommentPage(profileCommentPage)
                .build();
    }
}
